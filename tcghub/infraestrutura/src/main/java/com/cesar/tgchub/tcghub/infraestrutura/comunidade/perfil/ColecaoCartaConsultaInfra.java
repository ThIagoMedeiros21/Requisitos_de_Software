package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaColecaoResumo;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;

public class ColecaoCartaConsultaInfra implements IColecaoCartaConsulta {
	private static final List<CartaColecaoResumo> ACERVO_PADRAO = List.of(
			new CartaColecaoResumo(new CartaIdentificador("OP01-001"), "OP01-001", "Roronoa Zoro", "OP-01", "L",
					"OP-01 · Near Mint", "Vermelho", new BigDecimal("68.90"), Optional.empty()),
			new CartaColecaoResumo(new CartaIdentificador("OP01-016"), "OP01-016", "Nami", "OP-01", "R",
					"OP-01 · Near Mint", "Vermelho", new BigDecimal("18.50"), Optional.empty()),
			new CartaColecaoResumo(new CartaIdentificador("OP01-006"), "OP01-006", "Otama", "OP-01", "UC",
					"OP-01 · Near Mint", "Vermelho", new BigDecimal("9.90"), Optional.empty()),
			new CartaColecaoResumo(new CartaIdentificador("OP01-025"), "OP01-025", "Roronoa Zoro (Rush)", "OP-01", "SR",
					"OP-01 · Near Mint", "Vermelho", new BigDecimal("54.00"), Optional.empty()),
			new CartaColecaoResumo(new CartaIdentificador("OP05-119"), "OP05-119", "Monkey.D.Luffy Manga", "OP-05",
					"SEC", "OP-05 · Near Mint", "Roxo", new BigDecimal("490.00"), Optional.empty()));

	private static final Map<String, List<CartaColecaoResumo>> ACERVO_POR_USUARIO = Map.of("lucas", ACERVO_PADRAO,
			"ricardo", ACERVO_PADRAO, "thiago", ACERVO_PADRAO, "u", ACERVO_PADRAO, "a", ACERVO_PADRAO);

	@Override
	public boolean usuarioPossuiCarta(UsuarioIdentificador usuario, CartaIdentificador carta) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(carta);

		return listarCartasDisponiveis(usuario).stream().anyMatch(item -> item.getCarta().equals(carta));
	}

	@Override
	public List<CartaColecaoResumo> listarCartasDisponiveis(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return ACERVO_POR_USUARIO.getOrDefault(usuario.toString(), ACERVO_PADRAO);
	}
}
