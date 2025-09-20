package ar.edu.utn.dds.k3003.bll.consenso;

public enum ConsensoEnum {
    TODOS,
    AL_MENOS_2;

    public Consenso toConsenso() {
        switch (this) {
            case AL_MENOS_2: return new AlMenosDos();
            case TODOS: return new Todos();
            default: throw new IllegalArgumentException("No existe el consenso " + this);
        }
    }
}
