import { useMutation } from '@tanstack/react-query';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { Icon } from '../../../components/ui/Icon';
import { useToastStore } from '../../../stores/useToastStore';
import { entrar, mensagemErroAutenticacao } from '../api/authApi';

interface Credenciais {
  usuario: string;
  senha: string;
}

const CONTAS_DEMO: { rotulo: string; apelido: string; usuario: string; senha: string }[] = [
  { rotulo: 'Lucas', apelido: '@LuffyBR', usuario: 'lucas', senha: 'lucas' },
  { rotulo: 'Ricardo', apelido: '@ZoroTC', usuario: 'ricardo', senha: 'ricardo' },
  { rotulo: 'Thiago', apelido: '@SanjiOP', usuario: 'thiago', senha: 'thiago' },
];

/** Só aceita retorno para um caminho da própria aplicação, nunca para outro site. */
function destinoDeRetorno(retorno: string | null): string {
  return retorno?.startsWith('/') && !retorno.startsWith('//') ? retorno : '/comunidade/perfil';
}

export function LoginPage() {
  const navegar = useNavigate();
  const [parametros] = useSearchParams();
  const publicar = useToastStore((estado) => estado.publicar);
  const [erroLogin, definirErroLogin] = useState<string | null>(null);

  const retorno = parametros.get('retorno');
  const linkCadastro = retorno ? `/cadastro?retorno=${encodeURIComponent(retorno)}` : '/cadastro';

  const {
    register,
    handleSubmit,
    setValue,
    formState: { isValid },
  } = useForm<Credenciais>({ mode: 'onChange' });

  const { mutate, isPending } = useMutation({
    mutationFn: ({ usuario, senha }: Credenciais) => entrar(usuario.trim(), senha),
    onSuccess: () => {
      definirErroLogin(null);
      navegar(destinoDeRetorno(retorno), { replace: true });
    },
    onError: (erro) => {
      const msg = mensagemErroAutenticacao(erro);
      definirErroLogin(msg);
      publicar('erro', msg);
    },
  });

  function acessoRapido(usuario: string, senha: string) {
    setValue('usuario', usuario, { shouldValidate: true });
    setValue('senha', senha, { shouldValidate: true });
    mutate({ usuario, senha });
  }

  return (
    <div className="flex min-h-[calc(100vh-14rem)] items-center justify-center px-margin py-space-xl">
      <div className="w-full max-w-md space-y-space-lg rounded-2xl border border-ice-border bg-pure-white p-space-xl shadow-level-3">
        <div className="space-y-space-xs text-center">
          <div className="inline-flex items-center gap-space-xs font-headline-lg text-headline-lg tracking-tight text-primary">
            <Icon nome="style" className="text-[30px] text-ocean-blue" />
            <span>
              TCG<span className="font-extrabold text-ocean-blue">Hub</span>
            </span>
          </div>
          <h1 className="font-headline-md text-headline-md text-deep-navy">Entrar</h1>
          <p className="font-body-sm text-body-sm text-text-muted">
            Acesse sua conta de colecionador no Marketplace &amp; Comunidade One Piece TCG
          </p>
        </div>

        <div className="grid grid-cols-2 gap-1 rounded-xl bg-surface-container-low p-1">
          <span className="rounded-lg bg-deep-navy py-2 text-center font-label-md text-label-md font-semibold text-pure-white shadow-sm">
            Entrar
          </span>
          <Link
            to={linkCadastro}
            className="rounded-lg py-2 text-center font-label-md text-label-md font-semibold text-secondary no-underline transition-colors hover:bg-surface-container hover:text-deep-navy"
          >
            Criar Conta
          </Link>
        </div>

        {erroLogin && (
          <div
            role="alert"
            className="flex items-center gap-space-xs rounded-xl bg-error-container p-3 font-body-sm text-body-sm text-on-error-container"
          >
            <Icon nome="error" className="text-[18px] text-error" />
            <span>{erroLogin}</span>
          </div>
        )}

        <form
          onSubmit={handleSubmit((credenciais) => mutate(credenciais))}
          noValidate
          className="space-y-space-md"
        >
          <div className="space-y-1.5">
            <label htmlFor="usuario" className="block font-label-md text-label-md text-deep-navy">
              Usuário ou E-mail
            </label>
            <input
              id="usuario"
              autoFocus
              placeholder="Ex: lucas ou lucas@tcghub.com"
              {...register('usuario', { required: true })}
              className="w-full rounded-lg bg-surface-container-low px-3.5 py-2.5 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border transition-all outline-none placeholder:text-outline focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
            />
          </div>

          <div className="space-y-1.5">
            <label htmlFor="senha" className="block font-label-md text-label-md text-deep-navy">
              Senha
            </label>
            <input
              id="senha"
              type="password"
              placeholder="••••••••"
              {...register('senha', { required: true })}
              className="w-full rounded-lg bg-surface-container-low px-3.5 py-2.5 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border transition-all outline-none placeholder:text-outline focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
            />
          </div>

          <button
            type="submit"
            disabled={!isValid || isPending}
            className="flex w-full items-center justify-center gap-2 rounded-lg bg-ocean-blue py-3 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:opacity-50"
          >
            <Icon nome="login" className="text-[18px]" />
            <span>Entrar</span>
          </button>
        </form>

        <p className="text-center font-body-sm text-body-sm text-text-muted">
          Ainda não tem conta?{' '}
          <Link
            to={linkCadastro}
            className="font-semibold text-ocean-blue no-underline hover:text-deep-navy hover:underline"
          >
            Criar conta gratuita
          </Link>
        </p>

        <div className="space-y-space-xs border-t border-ice-border pt-space-md">
          <span className="block font-label-sm text-label-sm font-semibold tracking-wider text-secondary uppercase">
            Acesso Rápido (Demonstração)
          </span>
          <div className="grid grid-cols-3 gap-space-xs">
            {CONTAS_DEMO.map((conta) => (
              <button
                key={conta.usuario}
                type="button"
                disabled={isPending}
                onClick={() => acessoRapido(conta.usuario, conta.senha)}
                className="flex flex-col items-center rounded-lg border border-ice-border bg-surface-container-low px-2.5 py-2 text-center transition-colors hover:bg-sky-tint disabled:opacity-50"
              >
                <span className="font-label-sm text-label-sm font-bold text-deep-navy">
                  {conta.rotulo}
                </span>
                <span className="font-body-sm text-[11px] text-ocean-blue">{conta.apelido}</span>
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
