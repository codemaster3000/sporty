package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

/**
 * Created by sereGkaluv on 27-Nov-15.
 */
public class DTOPair<T extends IDTO, U extends IDTO> implements IDTO {
    private static final long serialVersionUID = 1L;

    private T _dto1;
    private U _dto2;

    public T getDTO1() {
        return _dto1;
    }

    public DTOPair setDTO1(T dto1) {
        _dto1 = dto1;
        return this;
    }

    public U getDTO2() {
        return _dto2;
    }

    public DTOPair setDTO2(U dto2) {
        _dto2 = dto2;
        return this;
    }
}
