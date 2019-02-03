package fi.esupponen.lottomachine;

import android.os.Binder;

class LocalBinder extends Binder {
    private LottoSearch service;

    public LocalBinder(LottoSearch lottoSearch) {
        service = lottoSearch;
    }

    public LottoSearch getService() {
        return service;
    }
}