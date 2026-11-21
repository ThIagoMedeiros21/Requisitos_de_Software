import { useMutation } from '@tanstack/react-query';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { Icon } from '../../../components/ui/Icon';
import { mensagemErro } from '../../../lib/api';
import { useToastStore } from '../../../stores/useToastStore';
import { cadastrarConta, type CadastrarContaEntrada } from '../api/authApi';

interface FormularioCadastro {
  usuario: string;
  apelido: string;
  email: string;
  cidade: string;
  senha: string;
  confirmarSenha: string;
}

function destinoDeRetorno(retorno: string | null): string {
  return retorno?.startsWith('/') && !retorno.startsWith('//') ? retorno : '/comunidade/perfil';
}

export function RegisterPage() {
  const navegar = useNavigate();
  const [parametros] = useSearchParams();
  const publicar = useToastStore((estado) => estado.publicar);
  const [erroCadastro, definirErroCadastro] = useState<string | null>(null);

  const retorno = parametros.get('retorno');
  const linkLogin = retorno ? `/login?retorno=${encodeURIComponent(retorno)}` : '/login';

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<FormularioCadastro>({
    defaultValues: {
      usuario: '',
      apelido: '',
      email: '',
      cidade: '',
      senha: '',
      confirmarSenha: '',
    },
  });

  const senhaDigitada = watch('senha') ?? '';

  const { mutate, isPending } = useMutation({
    mutationFn: (entrada: CadastrarContaEntrada) => cadastrarConta(entrada),
    onSuccess: () => {
      definirErroCadastro(null);
      publicar('sucesso', 'Conta criada e perfil comunitário configurado com sucesso!');
      navegar(destinoDeRetorno(retorno), { replace: true });
    },
    onError: (erro) => {
      const msg = mensagemErro(erro);
      definirErroCadastro(msg);
      publicar('erro', msg);
    },
  });

  function aoSubmeter(dados: FormularioCadastro) {
    definirErroCadastro(null);
    mutate({
      usuario: dados.usuario.trim().toLowerCase(),
      apelido: dados.apelido.trim().replace(/^@/, ''),
      email: dados.email.trim().toLowerCase(),
      senha: dados.senha,
      cidade: dados.cidade.trim() || null,
    });
  }

  const classeCampo =
    'w-full rounded-lg bg-surface-container-low px-3.5 py-2.5 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border transition-all outline-none placeholder:text-outline focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue';

  return (
    <div className="flex min-h-[calc(100vh-14rem)] items-center justify-center px-margin py-space-xl">
      <div className="w-full max-w-lg space-y-space-lg rounded-2xl border border-ice-border bg-pure-white p-space-xl shadow-level-3">
        <div className="space-y-space-xs text-center">
          <div className="inline-flex items-center gap-space-xs font-headline-lg text-headline-lg tracking-tight text-primary">
            <Icon nome="style" className="text-[30px] text-ocean-blue" />
            <span>
              TCG<span className="font-extrabold text-ocean-blue">Hub</span>
            </span>
          </div>
          <h1 className="font-headline-md text-headline-md text-deep-navy">Criar Conta</h1>
          <p className="font-body-sm text-body-sm text-text-muted">
            Cadastre sua conta e receba automaticamente seu Perfil Comunitário no TCGHub
          </p>
        </div>

        <div className="grid grid-cols-2 gap-1 rounded-xl bg-surface-container-low p-1">
          <Link
            to={linkLogin}
            className="rounded-lg py-2 text-center font-label-md text-label-md font-semibold text-secondary no-underline transition-colors hover:bg-surface-container hover:text-deep-navy"
          >
            Entrar
          </Link>
          <span className="rounded-lg bg-deep-navy py-2 text-center font-label-md text-label-md font-semibold text-pure-white shadow-sm">
            Criar Conta
          </span>
        </div>

        {erroCadastro && (
          <div
            role="alert"
            className="flex items-center gap-space-xs rounded-xl bg-error-container p-3 font-body-sm text-body-sm text-on-error-container"
          >
            <Icon nome="error" className="text-[18px] text-error" />
            <span>{erroCadastro}</span>
          </div>
        )}

        <form onSubmit={handleSubmit(aoSubmeter)} noValidate className="space-y-space-md">
          <div className="grid grid-cols-1 gap-space-md sm:grid-cols-2">
            <div className="space-y-1">
              <label
                htmlFor="cadastro-usuario"
                className="block font-label-md text-label-md text-deep-navy"
              >
                Usuário (login) *
              </label>
              <input
                id="cadastro-usuario"
                autoFocus
                placeholder="Ex: nami_navigator"
                {...register('usuario', {
                  validate: (valor) => {
                    const limpo = valor.trim();
                    if (limpo.length < 3) {
                      return 'Informe ao menos 3 caracteres.';
                    }
                    if (!/^[a-zA-Z0-9._-]+$/.test(limpo)) {
                      return 'Use apenas letras, números, ponto, traço ou sublinhado.';
                    }
                    return true;
                  },
                })}
                className={classeCampo}
              />
              {errors.usuario && (
                <p role="alert" className="font-body-sm text-body-sm font-semibold text-error">
                  {errors.usuario.message}
                </p>
              )}
            </div>

            <div className="space-y-1">
              <label
                htmlFor="cadastro-apelido"
                className="block font-label-md text-label-md text-deep-navy"
              >
                Apelido Comunitário (@) *
              </label>
              <input
                id="cadastro-apelido"
                placeholder="Ex: NamiNavigator"
                {...register('apelido', {
                  validate: (valor) =>
                    valor.trim().replace(/^@/, '').length > 0 || 'O apelido é obrigatório.',
                })}
                className={classeCampo}
              />
              {errors.apelido && (
                <p role="alert" className="font-body-sm text-body-sm font-semibold text-error">
                  {errors.apelido.message}
                </p>
              )}
            </div>
          </div>

          <div className="grid grid-cols-1 gap-space-md sm:grid-cols-2">
            <div className="space-y-1">
              <label
                htmlFor="cadastro-email"
                className="block font-label-md text-label-md text-deep-navy"
              >
                E-mail *
              </label>
              <input
                id="cadastro-email"
                type="email"
                placeholder="Ex: nami@tcghub.com"
                {...register('email', {
                  validate: (valor) =>
                    /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor.trim()) ||
                    'Informe um e-mail válido.',
                })}
                className={classeCampo}
              />
              {errors.email && (
                <p role="alert" className="font-body-sm text-body-sm font-semibold text-error">
                  {errors.email.message}
                </p>
              )}
            </div>

            <div className="space-y-1">
              <label
                htmlFor="cadastro-cidade"
                className="block font-label-md text-label-md text-deep-navy"
              >
                Cidade / Estado (opcional)
              </label>
              <input
                id="cadastro-cidade"
                placeholder="Ex: Recife, PE"
                {...register('cidade')}
                className={classeCampo}
              />
            </div>
          </div>

          <div className="grid grid-cols-1 gap-space-md sm:grid-cols-2">
            <div className="space-y-1">
              <label
                htmlFor="cadastro-senha"
                className="block font-label-md text-label-md text-deep-navy"
              >
                Senha *
              </label>
              <input
                id="cadastro-senha"
                type="password"
                placeholder="Mínimo 4 caracteres"
                {...register('senha', {
                  validate: (valor) =>
                    valor.length >= 4 || 'A senha deve ter ao menos 4 caracteres.',
                })}
                className={classeCampo}
              />
              {errors.senha && (
                <p role="alert" className="font-body-sm text-body-sm font-semibold text-error">
                  {errors.senha.message}
                </p>
              )}
            </div>

            <div className="space-y-1">
              <label
                htmlFor="cadastro-confirmar-senha"
                className="block font-label-md text-label-md text-deep-navy"
              >
                Confirmar Senha *
              </label>
              <input
                id="cadastro-confirmar-senha"
                type="password"
                placeholder="Repita a senha"
                {...register('confirmarSenha', {
                  validate: (valor) => valor === senhaDigitada || 'As senhas não coincidem.',
                })}
                className={classeCampo}
              />
              {errors.confirmarSenha && (
                <p role="alert" className="font-body-sm text-body-sm font-semibold text-error">
                  {errors.confirmarSenha.message}
                </p>
              )}
            </div>
          </div>

          <button
            type="submit"
            disabled={isPending}
            className="flex w-full items-center justify-center gap-2 rounded-lg bg-ocean-blue py-3 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:opacity-50"
          >
            <Icon nome="person_add" className="text-[18px]" />
            <span>Criar Conta e Entrar</span>
          </button>
        </form>

        <p className="text-center font-body-sm text-body-sm text-text-muted">
          Já possui uma conta?{' '}
          <Link
            to={linkLogin}
            className="font-semibold text-ocean-blue no-underline hover:text-deep-navy hover:underline"
          >
            Entrar na conta existente
          </Link>
        </p>
      </div>
    </div>
  );
}
