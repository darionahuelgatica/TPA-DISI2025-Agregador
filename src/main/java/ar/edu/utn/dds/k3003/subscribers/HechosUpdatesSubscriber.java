package ar.edu.utn.dds.k3003.subscribers;

import ar.edu.utn.dds.k3003.bll.operacion.*;
import ar.edu.utn.dds.k3003.exceptions.NonTransientException;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class HechosUpdatesSubscriber {

    private final UpsertHandler upsertHandler;
    private final DeleteHandler deleteHandler;
    private final DeleteAllHandler deleteAllHandler;
    private final PdiUpsertHandler pdiUpsertHandler;
    private final PdiDeleteHandler pdiDeleteHandler;
    private final PdiDeleteAllHandler pdiDeleteAllHandler;

    public HechosUpdatesSubscriber(
            UpsertHandler upsertHandler,
            DeleteHandler deleteHandler,
            DeleteAllHandler deleteAllHandler,
            PdiUpsertHandler pdiUpsertHandler,
            PdiDeleteHandler pdiDeleteHandler,
            PdiDeleteAllHandler pdiDeleteAllHandler) {
        this.upsertHandler = upsertHandler;
        this.deleteHandler = deleteHandler;
        this.deleteAllHandler = deleteAllHandler;
        this.pdiUpsertHandler = pdiUpsertHandler;
        this.pdiDeleteHandler = pdiDeleteHandler;
        this.pdiDeleteAllHandler = pdiDeleteAllHandler;
    }

    @RabbitListener(queues = "${app.hechos.updates.queue}")
    public void onHechoUpdate(Message msg, Channel ch) throws Exception {
        long tag = msg.getMessageProperties().getDeliveryTag();
        try {
            var operacionRecibida = msg.getMessageProperties().getHeader("op");

            if (operacionRecibida == null)
                throw new NonTransientException("Mensaje sin codigo de operacion");

            OperacionEnum operacion = OperacionEnum.GetFromString(operacionRecibida.toString());
            var msgString = toString(msg);

            switch (operacion) {
                case UPSERT         -> this.upsertHandler.handle(msgString);
                case DELETE         -> this.deleteHandler.handle(msgString);
                case DELETEALL      -> this.deleteAllHandler.handle(msgString);
                case PDIUPSERT      -> this.pdiUpsertHandler.handle(msgString);
                case PDIDELETE      -> this.pdiDeleteHandler.handle(msgString);
                case PDIDELETEALL   -> this.pdiDeleteAllHandler.handle(msgString);
            }

            ch.basicAck(tag, false);
        } catch (NonTransientException e) {
            ch.basicReject(tag, false);
            throw e;
        }
        catch (Exception e) {
            ch.basicNack(tag, false, true); // requeue
            throw e;
        }
    }

    private String toString(Message msg) {
        final var props = msg.getMessageProperties();
        final String ctype = props.getContentType();
        if (ctype != null && !ctype.contains("json")) {
            throw new NonTransientException("Content-Type no soportado: " + ctype);
        }

        final String enc = props.getContentEncoding();
        final Charset cs = enc == null ? StandardCharsets.UTF_8 : Charset.forName(enc);
        return new String(msg.getBody(), cs);
    }
}
