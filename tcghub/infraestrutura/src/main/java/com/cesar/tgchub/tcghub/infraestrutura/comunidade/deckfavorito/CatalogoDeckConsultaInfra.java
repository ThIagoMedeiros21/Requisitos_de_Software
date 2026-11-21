package com.cesar.tgchub.tcghub.infraestrutura.comunidade.deckfavorito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.ComposicaoDeck;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.ItemComposicaoDeck;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi.ICatalogoDeckConsulta;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;

public class CatalogoDeckConsultaInfra implements ICatalogoDeckConsulta {
	private static final String BASE_IMG = "https://optcgapi.com/media/static/Card_Images/";

	private static final List<ComposicaoDeck> CATALOGO = List.of(criarSt01StrawHatCrew(), criarSt10TheThreeCaptains(),
			criarSt04AnimalKingdomPirates(), criarOp01RedZoroRush(), criarOp01BlueCrocodileControl(),
			criarSt09YamatoInativo());

	@Override
	public List<ComposicaoDeck> pesquisarAtivos(String termo) {
		Objects.requireNonNull(termo);
		var filtro = termo.trim().toLowerCase(Locale.ROOT);
		return CATALOGO.stream().filter(ComposicaoDeck::isAtivo)
				.filter(deck -> filtro.isEmpty()
						|| deck.getDeck().toString().toLowerCase(Locale.ROOT).contains(filtro)
						|| deck.getNomeDeck().toLowerCase(Locale.ROOT).contains(filtro)
						|| deck.getLider().toLowerCase(Locale.ROOT).contains(filtro)
						|| deck.getCor().toLowerCase(Locale.ROOT).contains(filtro))
				.toList();
	}

	@Override
	public Optional<ComposicaoDeck> buscarPorIdentificador(DeckIdentificador deck) {
		Objects.requireNonNull(deck);
		return CATALOGO.stream().filter(item -> item.getDeck().equals(deck)).findFirst();
	}

	private static ItemComposicaoDeck item(String codigo, String nome, String setId, String setName, String rarity,
			String color, String type, @Nullable String cost, @Nullable String power, @Nullable String subTypes,
			@Nullable String counter, @Nullable String attribute, int quantidade, String marketPrice,
			String inventoryPrice) {
		return new ItemComposicaoDeck(new CartaIdentificador(codigo), nome, setId, setName, rarity, color, type,
				Optional.ofNullable(cost), Optional.ofNullable(power), Optional.ofNullable(subTypes),
				Optional.ofNullable(counter), Optional.ofNullable(attribute), quantidade, new BigDecimal(marketPrice),
				new BigDecimal(inventoryPrice), codigo, BASE_IMG + codigo + ".jpg");
	}

	private static ComposicaoDeck criarSt01StrawHatCrew() {
		return new ComposicaoDeck(new DeckIdentificador("ST01"), "Straw Hat Crew", "Monkey.D.Luffy (ST01-001)",
				"ST01-001", BASE_IMG + "ST01-001.jpg", "Red", true,
				List.of(item("ST01-001", "Monkey.D.Luffy", "ST-01", "Straw Hat Crew", "L", "Red", "Leader", null,
						"5000", "Supernovas / Straw Hat Crew", null, "Strike", 1, "12.50", "10.00"),
						item("ST01-007", "Nami", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "1", "1000",
								"Straw Hat Crew", "1000", "Special", 4, "4.20", "3.50"),
						item("ST01-006", "Tony Tony.Chopper", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "1",
								"1000", "Animal / Straw Hat Crew", null, "Strike", 4, "3.90", "3.10"),
						item("ST01-002", "Usopp", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "2", "2000",
								"Straw Hat Crew", "1000", "Ranged", 4, "2.50", "2.00"),
						item("ST01-004", "Sanji", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "2", "4000",
								"Straw Hat Crew", null, "Strike", 4, "5.10", "4.40"),
						item("ST01-011", "Brook", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "2", "3000",
								"Straw Hat Crew", "2000", "Slash", 4, "8.90", "7.80"),
						item("ST01-013", "Roronoa Zoro", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "3",
								"5000", "Supernovas / Straw Hat Crew", null, "Slash", 4, "7.40", "6.20"),
						item("ST01-008", "Nico Robin", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "3", "5000",
								"Straw Hat Crew", "1000", "Wisdom", 4, "3.10", "2.50"),
						item("ST01-010", "Franky", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "4", "6000",
								"Straw Hat Crew", "1000", "Strike", 4, "2.80", "2.20"),
						item("ST01-005", "Jinbe", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "3", "5000",
								"Fish-Man / Straw Hat Crew", null, "Strike", 4, "2.40", "1.90"),
						item("ST01-012", "Monkey.D.Luffy", "ST-01", "Straw Hat Crew", "SR", "Red", "Character", "5",
								"6000", "Supernovas / Straw Hat Crew", null, "Strike", 4, "16.50", "14.20"),
						item("ST01-014", "Guard Point", "ST-01", "Straw Hat Crew", "C", "Red", "Event", "1", null,
								"Animal / Straw Hat Crew", null, null, 4, "4.50", "3.80"),
						item("ST01-015", "Gum-Gum Jet Pistol", "ST-01", "Straw Hat Crew", "C", "Red", "Event", "4",
								null, "Supernovas / Straw Hat Crew", null, null, 4, "9.80", "8.40"),
						item("ST01-017", "Thousand Sunny", "ST-01", "Straw Hat Crew", "C", "Red", "Stage", "2", null,
								"Straw Hat Crew", null, null, 2, "3.60", "3.00")));
	}

	private static ComposicaoDeck criarSt10TheThreeCaptains() {
		return new ComposicaoDeck(new DeckIdentificador("ST10"), "The Three Captains", "Trafalgar Law (ST10-001)",
				"ST10-001", BASE_IMG + "ST10-001.jpg", "Red/Purple", true,
				List.of(item("ST10-001", "Trafalgar Law", "ST-10", "The Three Captains", "L", "Red/Purple", "Leader",
						null, "5000", "Heart Pirates", null, "Slash", 1, "19.90", "17.00"),
						item("ST10-004", "Sanji", "ST-10", "The Three Captains", "C", "Red", "Character", "6", "6000",
								"Straw Hat Crew", "1000", "Strike", 4, "3.20", "2.60"),
						item("ST10-005", "Jinbe", "ST-10", "The Three Captains", "C", "Red", "Character", "2", "2000",
								"Fish-Man / Straw Hat Crew", "2000", "Strike", 4, "4.50", "3.80"),
						item("ST10-006", "Monkey.D.Luffy", "ST-10", "The Three Captains", "SR", "Red", "Character", "10",
								"11000", "Straw Hat Crew", null, "Strike", 4, "14.80", "12.50"),
						item("ST10-007", "Killer", "ST-10", "The Three Captains", "C", "Purple", "Character", "5",
								"6000", "Kid Pirates", "1000", "Slash", 4, "2.90", "2.30"),
						item("ST10-008", "Shachi & Penguin", "ST-10", "The Three Captains", "C", "Purple", "Character",
								"4", "5000", "Heart Pirates", "2000", "Ranged", 4, "7.80", "6.50"),
						item("ST10-009", "Jean Bart", "ST-10", "The Three Captains", "C", "Purple", "Character", "4",
								"5000", "Heart Pirates", "1000", "Strike", 4, "3.40", "2.80"),
						item("ST10-010", "Trafalgar Law", "ST-10", "The Three Captains", "SR", "Purple", "Character",
								"4", "5000", "Heart Pirates", "1000", "Slash", 4, "18.50", "16.00"),
						item("ST10-011", "Heat", "ST-10", "The Three Captains", "C", "Purple", "Character", "3", "4000",
								"Kid Pirates", "1000", "Special", 4, "2.10", "1.70"),
						item("ST10-012", "Bepo", "ST-10", "The Three Captains", "C", "Purple", "Character", "4", "5000",
								"Minks / Heart Pirates", "1000", "Strike", 4, "4.90", "4.10"),
						item("ST10-013", "Eustass\"Captain\"Kid", "ST-10", "The Three Captains", "SR", "Purple",
								"Character", "7", "8000", "Kid Pirates", null, "Special", 4, "22.00", "19.00"),
						item("ST10-014", "Wire", "ST-10", "The Three Captains", "C", "Purple", "Character", "3", "3000",
								"Kid Pirates", "1000", "Slash", 4, "2.60", "2.10"),
						item("ST10-015", "Gum-Gum Giant Sumo Slap", "ST-10", "The Three Captains", "C", "Red", "Event",
								"1", null, "Straw Hat Crew", null, null, 4, "3.10", "2.50"),
						item("ST10-016", "Gum-Gum Kong Gatling", "ST-10", "The Three Captains", "C", "Red", "Event",
								"5", null, "Straw Hat Crew", null, null, 2, "3.80", "3.00")));
	}

	private static ComposicaoDeck criarSt04AnimalKingdomPirates() {
		return new ComposicaoDeck(new DeckIdentificador("ST04"), "Animal Kingdom Pirates", "Kaido (ST04-001)",
				"ST04-001", BASE_IMG + "ST04-001.jpg", "Purple", true,
				List.of(item("ST04-001", "Kaido", "ST-04", "Animal Kingdom Pirates", "L", "Purple", "Leader", null,
						"5000", "The Four Emperors / Animal Kingdom Pirates", null, "Strike", 1, "11.00", "9.50"),
						item("ST04-002", "Ulti", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Character", "4",
								"5000", "Animal Kingdom Pirates", "2000", "Strike", 4, "6.40", "5.50"),
						item("ST04-003", "Kaido", "ST-04", "Animal Kingdom Pirates", "SR", "Purple", "Character", "9",
								"10000", "The Four Emperors / Animal Kingdom Pirates", null, "Strike", 4, "19.20",
								"16.50"),
						item("ST04-004", "King", "ST-04", "Animal Kingdom Pirates", "SR", "Purple", "Character", "6",
								"7000", "Animal Kingdom Pirates", null, "Special", 4, "14.50", "12.00"),
						item("ST04-005", "Queen", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Character", "5",
								"6000", "Animal Kingdom Pirates", "1000", "Strike", 4, "38.00", "34.00"),
						item("ST04-006", "Sasaki", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Character", "3",
								"4000", "Animal Kingdom Pirates", "1000", "Strike", 4, "2.30", "1.90"),
						item("ST04-008", "Jack", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Character", "3",
								"4000", "Animal Kingdom Pirates", "1000", "Strike", 4, "4.10", "3.40"),
						item("ST04-010", "Who's.Who", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Character",
								"3", "3000", "Animal Kingdom Pirates", null, "Slash", 4, "3.60", "2.90"),
						item("ST04-011", "Black Maria", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Character",
								"2", "2000", "Animal Kingdom Pirates", "1000", "Special", 4, "2.80", "2.20"),
						item("ST04-012", "Page One", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Character", "4",
								"6000", "Animal Kingdom Pirates", "1000", "Strike", 4, "2.50", "2.00"),
						item("ST04-014", "Lead Performer \"Disaster\"", "ST-04", "Animal Kingdom Pirates", "C",
								"Purple", "Event", "4", null, "Animal Kingdom Pirates", null, null, 4, "3.00", "2.40"),
						item("ST04-016", "Blast Breath", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Event", "1",
								null, "The Four Emperors / Animal Kingdom Pirates", null, null, 4, "5.20", "4.40"),
						item("ST04-015", "Brachio Bomber", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Event",
								"6", null, "Animal Kingdom Pirates", null, null, 2, "2.40", "1.90"),
						item("ST04-017", "Onigashima Island", "ST-04", "Animal Kingdom Pirates", "C", "Purple", "Stage",
								"3", null, "Animal Kingdom Pirates", null, null, 4, "9.50", "8.10")));
	}

	private static ComposicaoDeck criarOp01RedZoroRush() {
		return new ComposicaoDeck(new DeckIdentificador("OP01-ZORO"), "Red Zoro Rush", "Roronoa Zoro (OP01-001)",
				"OP01-001", BASE_IMG + "OP01-001.jpg", "Red", true,
				List.of(item("OP01-001", "Roronoa Zoro", "OP-01", "Romance Dawn", "L", "Red", "Leader", null, "5000",
						"Supernovas / Straw Hat Crew", null, "Slash", 1, "68.90", "62.00"),
						item("OP01-016", "Nami", "OP-01", "Romance Dawn", "R", "Red", "Character", "1", "2000",
								"Straw Hat Crew", "1000", "Special", 4, "18.50", "15.90"),
						item("OP01-006", "Otama", "OP-01", "Romance Dawn", "UC", "Red", "Character", "1", "0",
								"Land of Wano", "2000", "Special", 4, "9.90", "8.00"),
						item("OP01-025", "Roronoa Zoro", "OP-01", "Romance Dawn", "SR", "Red", "Character", "3", "5000",
								"Supernovas / Straw Hat Crew", null, "Slash", 4, "54.00", "48.50"),
						item("OP01-013", "Sanji", "OP-01", "Romance Dawn", "R", "Red", "Character", "2", "3000",
								"Straw Hat Crew", "2000", "Strike", 4, "12.00", "10.20"),
						item("OP01-017", "Nico Robin", "OP-01", "Romance Dawn", "R", "Red", "Character", "3", "4000",
								"Straw Hat Crew", "1000", "Strike", 4, "8.40", "7.10"),
						item("OP01-015", "Tony Tony.Chopper", "OP-01", "Romance Dawn", "UC", "Red", "Character", "3",
								"4000", "Animal / Straw Hat Crew", "1000", "Strike", 4, "4.20", "3.50"),
						item("OP01-022", "Brook", "OP-01", "Romance Dawn", "UC", "Red", "Character", "4", "5000",
								"Straw Hat Crew", "1000", "Slash", 4, "5.80", "4.90"),
						item("OP01-024", "Monkey.D.Luffy", "OP-01", "Romance Dawn", "SR", "Red", "Character", "2",
								"3000", "Supernovas / Straw Hat Crew", "1000", "Strike", 4, "19.50", "17.00"),
						item("ST01-006", "Tony Tony.Chopper", "ST-01", "Straw Hat Crew", "C", "Red", "Character", "1",
								"1000", "Animal / Straw Hat Crew", null, "Strike", 4, "3.90", "3.10"),
						item("OP01-029", "Radical Beam!!", "OP-01", "Romance Dawn", "UC", "Red", "Event", "1", null,
								"Straw Hat Crew", null, null, 4, "7.90", "6.80"),
						item("ST01-015", "Gum-Gum Jet Pistol", "ST-01", "Straw Hat Crew", "C", "Red", "Event", "4",
								null, "Supernovas / Straw Hat Crew", null, null, 4, "9.80", "8.40"),
						item("OP01-026", "Gum-Gum Fire-Fist Pistol Red Hawk", "OP-01", "Romance Dawn", "R", "Red",
								"Event", "2", null, "Supernovas / Straw Hat Crew", null, null, 4, "6.50", "5.50"),
						item("OP01-120", "Shanks", "OP-01", "Romance Dawn", "SEC", "Red", "Character", "9", "10000",
								"Four Emperors / Red-Haired Pirates", null, "Slash", 2, "320.00", "295.00")));
	}

	private static ComposicaoDeck criarOp01BlueCrocodileControl() {
		return new ComposicaoDeck(new DeckIdentificador("OP01-CROC"), "Blue Crocodile Control", "Crocodile (OP01-062)",
				"OP01-062", BASE_IMG + "OP01-062.jpg", "Blue", true,
				List.of(item("OP01-062", "Crocodile", "OP-01", "Romance Dawn", "L", "Blue/Purple", "Leader", null,
						"5000", "The Seven Warlords of the Sea / Baroque Works", null, "Special", 1, "15.00", "13.00"),
						item("OP01-067", "Crocodile", "OP-01", "Romance Dawn", "SR", "Blue", "Character", "7", "7000",
								"The Seven Warlords of the Sea / Baroque Works", "1000", "Special", 4, "24.00",
								"20.50"),
						item("OP01-070", "Dracule Mihawk", "OP-01", "Romance Dawn", "SR", "Blue", "Character", "9",
								"9000", "The Seven Warlords of the Sea", null, "Slash", 4, "45.00", "40.00"),
						item("OP01-073", "Donquixote Doflamingo", "OP-01", "Romance Dawn", "R", "Blue", "Character", "3",
								"4000", "The Seven Warlords of the Sea / Donquixote Pirates", "1000", "Special", 4,
								"14.00", "12.00"),
						item("OP01-077", "Perona", "OP-01", "Romance Dawn", "UC", "Blue", "Character", "1", "2000",
								"Thriller Bark Pirates", "1000", "Special", 4, "8.90", "7.50"),
						item("OP01-078", "Boa Hancock", "OP-01", "Romance Dawn", "SR", "Blue", "Character", "4", "5000",
								"The Seven Warlords of the Sea / Kuja Pirates", "1000", "Special", 4, "32.00", "28.00"),
						item("OP01-079", "Ms. All Sunday", "OP-01", "Romance Dawn", "R", "Blue", "Character", "3",
								"1000", "Baroque Works", "1000", "Wisdom", 4, "6.20", "5.10"),
						item("OP01-064", "Alvida", "OP-01", "Romance Dawn", "UC", "Blue", "Character", "2", "3000",
								"Buggy's Delivery", "2000", "Strike", 4, "5.50", "4.60"),
						item("OP01-074", "Bartholomew Kuma", "OP-01", "Romance Dawn", "R", "Blue", "Character", "4",
								"5000", "The Seven Warlords of the Sea / Revolutionary Army", null, "Strike", 4, "4.80",
								"3.90"),
						item("OP01-075", "Pacifista", "OP-01", "Romance Dawn", "C", "Blue", "Character", "4", "5000",
								"Biological Weapon / Navy", "1000", "Special", 4, "2.10", "1.60"),
						item("OP01-086", "Overheat", "OP-01", "Romance Dawn", "R", "Blue", "Event", "2", null,
								"The Seven Warlords of the Sea / Donquixote Pirates", null, null, 4, "4.90", "4.10"),
						item("OP01-088", "Desert Spada", "OP-01", "Romance Dawn", "UC", "Blue", "Event", "1", null,
								"The Seven Warlords of the Sea / Baroque Works", null, null, 4, "3.70", "3.00"),
						item("OP01-087", "Officer Agents", "OP-01", "Romance Dawn", "C", "Blue", "Event", "2", null,
								"Baroque Works", null, null, 4, "2.40", "1.90"),
						item("OP01-090", "Baroque Works", "OP-01", "Romance Dawn", "C", "Blue", "Event", "1", null,
								"Baroque Works", null, null, 2, "3.10", "2.50")));
	}

	private static ComposicaoDeck criarSt09YamatoInativo() {
		return new ComposicaoDeck(new DeckIdentificador("ST09-INATIVO"), "Yamato Wano (Inativo)", "Yamato (ST09-001)",
				"ST09-001", BASE_IMG + "ST09-001.jpg", "Yellow", false,
				List.of(item("ST09-001", "Yamato", "ST-09", "Yamato", "L", "Yellow", "Leader", null, "5000",
						"Land of Wano", null, "Strike", 1, "10.00", "8.50"),
						item("ST09-005", "Kouzuki Oden", "ST-09", "Yamato", "SR", "Yellow", "Character", "7", "7000",
								"Land of Wano / Kouzuki Clan", null, "Slash", 4, "12.00", "10.00")));
	}
}
