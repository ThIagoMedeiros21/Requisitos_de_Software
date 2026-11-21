package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaColecaoResumo;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;

public class ColecaoCartaConsultaInfra implements IColecaoCartaConsulta {
	private static final String BASE_IMG = "https://optcgapi.com/media/static/Card_Images/";

	private static final List<CartaColecaoResumo> ACERVO_PADRAO = List.of(
			new CartaColecaoResumo(new CartaIdentificador("OP01-001"), "OP01-001", "Roronoa Zoro", "OP-01",
					"Romance Dawn", "L", "OP-01 · Near Mint", "Red", "Leader", "0", "5000",
					"Straw Hat Crew Supernovas", 0, "Slash", new BigDecimal("68.90"), new BigDecimal("62.00"),
					"OP01-001", Optional.of(BASE_IMG + "OP01-001.jpg")),
			new CartaColecaoResumo(new CartaIdentificador("OP01-016"), "OP01-016", "Nami", "OP-01", "Romance Dawn",
					"R", "OP-01 · Near Mint", "Red", "Character", "1", "2000", "Straw Hat Crew", 1000, "Special",
					new BigDecimal("18.50"), new BigDecimal("15.90"), "OP01-016",
					Optional.of(BASE_IMG + "OP01-016.jpg")),
			new CartaColecaoResumo(new CartaIdentificador("OP01-006"), "OP01-006", "Otama", "OP-01", "Romance Dawn",
					"UC", "OP-01 · Near Mint", "Red", "Character", "1", "0", "Land of Wano", 2000, "Special",
					new BigDecimal("9.90"), new BigDecimal("8.00"), "OP01-006",
					Optional.of(BASE_IMG + "OP01-006.jpg")),
			new CartaColecaoResumo(new CartaIdentificador("OP01-025"), "OP01-025", "Roronoa Zoro (Rush)", "OP-01",
					"Romance Dawn", "SR", "OP-01 · Near Mint", "Red", "Character", "3", "5000",
					"Straw Hat Crew Supernovas", 0, "Slash", new BigDecimal("54.00"), new BigDecimal("48.50"),
					"OP01-025", Optional.of(BASE_IMG + "OP01-025.jpg")),
			new CartaColecaoResumo(new CartaIdentificador("OP01-047"), "OP01-047", "Trafalgar Law", "OP-01",
					"Romance Dawn", "SR", "OP-01 · Near Mint", "Green", "Character", "5", "6000",
					"Supernovas Heart Pirates", 0, "Slash", new BigDecimal("42.00"), new BigDecimal("38.00"),
					"OP01-047", Optional.of(BASE_IMG + "OP01-047.jpg")),
			new CartaColecaoResumo(new CartaIdentificador("OP01-120"), "OP01-120", "Shanks", "OP-01", "Romance Dawn",
					"SEC", "OP-01 · Near Mint", "Red", "Character", "9", "10000",
					"Four Emperors Red-Haired Pirates", 0, "Slash", new BigDecimal("320.00"),
					new BigDecimal("295.00"), "OP01-120", Optional.of(BASE_IMG + "OP01-120.jpg")),
			new CartaColecaoResumo(new CartaIdentificador("OP05-119"), "OP05-119", "Monkey.D.Luffy Manga", "OP-05",
					"Awakening of the New Era", "SEC", "OP-05 · Near Mint", "Purple", "Character", "10", "12000",
					"Four Emperors Straw Hat Crew", 0, "Strike", new BigDecimal("490.00"), new BigDecimal("450.00"),
					"OP05-119_p1", Optional.of(BASE_IMG + "OP05-119_p1.jpg")),
			new CartaColecaoResumo(new CartaIdentificador("OP02-013"), "OP02-013", "Portgas.D.Ace", "OP-02",
					"Paramount War", "SR", "OP-02 · Near Mint", "Red", "Character", "7", "7000",
					"Whitebeard Pirates", 0, "Special", new BigDecimal("75.00"), new BigDecimal("69.00"),
					"OP02-013", Optional.of(BASE_IMG + "OP02-013.jpg")),
			new CartaColecaoResumo(new CartaIdentificador("OP03-123"), "OP03-123", "Charlotte Katakuri", "OP-03",
					"Pillars of Strength", "SEC", "OP-03 · Near Mint", "Yellow", "Character", "8", "8000",
					"Big Mom Pirates", 0, "Strike", new BigDecimal("110.00"), new BigDecimal("98.00"),
					"OP03-123", Optional.of(BASE_IMG + "OP03-123.jpg")));

	@Override
	public boolean usuarioPossuiCarta(UsuarioIdentificador usuario, CartaIdentificador carta) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(carta);

		return listarCartasDisponiveis(usuario).stream().anyMatch(item -> item.getCarta().equals(carta));
	}

	@Override
	public List<CartaColecaoResumo> listarCartasDisponiveis(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return ACERVO_PADRAO;
	}
}
