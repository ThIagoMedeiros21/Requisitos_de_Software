package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "PCM_PERFIL_COMUNITARIO")
public class PerfilComunitarioJpa implements IAgregadoJpa<PerfilComunitario> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PCM_ID", updatable = false)
	private @Nullable Long identificador;

	@Column(name = "PCM_USR_ID", unique = true)
	@NotNull
	@Size(max = UsuarioIdentificador.TAMANHO)
	private String usuario;

	@Column(name = "PCM_APELIDO", unique = true)
	@NotNull
	@Size(max = Apelido.TAMANHO)
	private String apelido;

	@Column(name = "PCM_CIDADE")
	@Size(max = PerfilComunitario.TAMANHO_CIDADE)
	private @Nullable String cidade;

	@Column(name = "PCM_DESCRICAO")
	@Size(max = PerfilComunitario.TAMANHO_DESCRICAO)
	private @Nullable String descricao;

	@Column(name = "PCM_FOTO")
	@Size(max = PerfilComunitario.TAMANHO_FOTO)
	private @Nullable String foto;

	@Column(name = "PCM_PREFERENCIAS_PUBLICAS")
	@Size(max = PerfilComunitario.TAMANHO_PREFERENCIAS_PUBLICAS)
	private @Nullable String preferenciasPublicas;

	@Column(name = "PCM_DECKS_FAVORITOS_VISIVEIS")
	private boolean decksFavoritosVisiveis;

	@OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@OrderBy("indice ASC")
	private List<PerfilCartaFavoritaJpa> cartasFavoritas = new ArrayList<>();

	protected PerfilComunitarioJpa() {
	}

	public PerfilComunitarioJpa(@Nullable Long identificador, String usuario, String apelido, @Nullable String cidade,
			@Nullable String descricao, @Nullable String foto, @Nullable String preferenciasPublicas,
			boolean decksFavoritosVisiveis) {
		this.identificador = identificador;
		this.usuario = usuario;
		this.apelido = apelido;
		this.cidade = cidade;
		this.descricao = descricao;
		this.foto = foto;
		this.preferenciasPublicas = preferenciasPublicas;
		this.decksFavoritosVisiveis = decksFavoritosVisiveis;
	}

	public @Nullable Long getIdentificador() {
		return identificador;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getApelido() {
		return apelido;
	}

	public @Nullable String getCidade() {
		return cidade;
	}

	public @Nullable String getDescricao() {
		return descricao;
	}

	public @Nullable String getFoto() {
		return foto;
	}

	public @Nullable String getPreferenciasPublicas() {
		return preferenciasPublicas;
	}

	public boolean isDecksFavoritosVisiveis() {
		return decksFavoritosVisiveis;
	}

	public List<PerfilCartaFavoritaJpa> getCartasFavoritas() {
		return cartasFavoritas;
	}

	public void atualizarDados(String apelido, @Nullable String cidade, @Nullable String descricao,
			@Nullable String foto, @Nullable String preferenciasPublicas, boolean decksFavoritosVisiveis,
			List<String> cartas) {
		this.apelido = apelido;
		this.cidade = cidade;
		this.descricao = descricao;
		this.foto = foto;
		this.preferenciasPublicas = preferenciasPublicas;
		this.decksFavoritosVisiveis = decksFavoritosVisiveis;
		substituirCartasFavoritas(cartas);
	}

	public void substituirCartasFavoritas(List<String> cartas) {
		this.cartasFavoritas.clear();
		for (int i = 0; i < cartas.size(); i++) {
			this.cartasFavoritas.add(new PerfilCartaFavoritaJpa(this, i, cartas.get(i)));
		}
	}
}
