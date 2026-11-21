package com.cesar.tgchub.tcghub.dominio.adm.abertura;

public enum StatusAbertura {
	ATIVA, INATIVA;

	public boolean isAtiva() {
		return this == ATIVA;
	}

	public static StatusAbertura deBooleano(boolean ativa) {
		return ativa ? ATIVA : INATIVA;
	}
}
