package ar.edu.utn.dds.k3003.subscribers;

import ar.edu.utn.dds.k3003.exceptions.NonTransientException;
import ar.edu.utn.dds.k3003.bll.operacion.DeleteHandler;
import ar.edu.utn.dds.k3003.bll.operacion.OperacionEnum;
import ar.edu.utn.dds.k3003.bll.operacion.PdiHandler;
import ar.edu.utn.dds.k3003.bll.operacion.UpsertHandler;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class HechosUpdatesSubscriber {

    private final DeleteHandler deleteHandler;
    private final PdiHandler pdiHandler;
    private final UpsertHandler upsertHandler;

    public HechosUpdatesSubscriber(DeleteHandler deleteHandler, PdiHandler pdiHandler, UpsertHandler upsertHandler) {
        this.deleteHandler = deleteHandler;
        this.pdiHandler = pdiHandler;
        this.upsertHandler = upsertHandler;
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
                case UPSERT -> this.upsertHandler.handle(msgString);
                case PDI    -> this.pdiHandler.handle(msgString);
                case DELETE -> this.deleteHandler.handle(msgString);
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
