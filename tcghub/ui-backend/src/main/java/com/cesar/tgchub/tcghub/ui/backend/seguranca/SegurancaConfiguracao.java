package com.cesar.tgchub.tcghub.ui.backend.seguranca;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import com.cesar.tgchub.tcghub.dominio.IUsuarioContaRepositorio;

import dev.sauloaraujo.d5.framework.ui.Backend;
import dev.sauloaraujo.d5.framework.ui.seguranca.SegurancaConfiguracaoAbstrata;

@Configuration
public class SegurancaConfiguracao extends SegurancaConfiguracaoAbstrata {
	private static final String CADASTRO = "/" + Backend.PREFIXO + "autenticacao/cadastro";

	@Override
	@Bean
	public SecurityFilterChain filtroSeguranca(HttpSecurity http) {
		Objects.requireNonNull(http);
		http.csrf(csrf -> csrf.ignoringRequestMatchers(CADASTRO));
		return super.filtroSeguranca(http);
	}

	@Override
	protected void autorizar(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry autorizacao) {
		Objects.requireNonNull(autorizacao);
		autorizacao.requestMatchers(CADASTRO).permitAll();
		autorizacao.requestMatchers(BACKEND).authenticated();
	}

	@Bean
	UserDetailsService usuarios(IUsuarioContaRepositorio contaRepositorio) {
		Objects.requireNonNull(contaRepositorio);
		return identificador -> {
			var conta = contaRepositorio.buscarPorUsuarioOuEmailIgnoreCase(identificador)
					.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + identificador));
			return User.withUsername(conta.getUsuario().toString()).password(conta.getSenhaHash())
					.authorities(conta.getAutorizacao().name()).build();
		};
	}
}
