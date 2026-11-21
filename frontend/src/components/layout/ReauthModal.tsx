import { useState } from 'react';
import { useForm } from 'react-hook-form';
import {
  autenticar,
  encerrarSessao,
  mensagemErroAutenticacao,
  usuarioAtual,
} from '../../features/auth/api/authApi';
import { chaveAutenticacao, clienteConsultas } from '../../lib/queryClient';
import { useAuthModalStore } from '../../stores/useAuthModalStore';
import { Icon } from '../ui/Icon';

interface Credenciais {
  usuario: string;
  senha: string;
}

function ReauthForm({ aoConcluir, aoCancelar }: { aoConcluir: () => void; aoCancelar: () => void }) {
  // Capturado na abertura do modal, antes de qualquer nova tentativa de login — impede que outra
  // pessoa reautentique com a própria conta e submeta, sem perceber, os dados que o usuário
  // original deixou preenchidos no formulário.
  const [usuarioAnterior] = useState(() => usuarioAtual()?.usuario ?? null);
  const [mensagem, definirMensagem] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    formState: { isValid, isSubmitting },
  } = useForm<Credenciais>({ mode: 'onChange' });

  async function entrar({ usuario, senha }: Credenciais) {
    definirMensagem(null);
    try {
      const autenticado = await autenticar(usuario, senha);

      if (usuarioAnterior && autenticado.usuario !== usuarioAnterior) {
        // A sessão no servidor já é da outra pessoa: encerra-a sem tocar no estado da aplicação.
        await encerrarSessao();
        definirMensagem(`Entre com o mesmo usuário que estava usando (${usuarioAnterior}).`);
        return;
      }

      clienteConsultas.setQueryData(chaveAutenticacao, autenticado);
      aoConcluir();
    } catch (erro) {
      definirMensagem(mensagemErroAutenticacao(erro));
    }
  }

  const classeCampo =
    'w-full rounded-lg bg-surface-container-low px-3 py-2.5 font-body-md text-body-md text-deep-navy outline-none ring-1 ring-steel-border focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue';

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="reauth-titulo"
      className="fixed inset-0 z-[70] flex items-center justify-center bg-deep-navy/60 p-4 backdrop-blur-sm"
    >
      <div className="w-full max-w-md space-y-space-md rounded-2xl bg-pure-white p-space-lg shadow-level-4">
        <div className="flex items-center gap-space-sm">
          <span className="flex h-11 w-11 items-center justify-center rounded-xl bg-sky-tint text-ocean-blue">
            <Icon nome="lock_clock" className="text-[24px]" />
          </span>
          <h1 id="reauth-titulo" className="font-headline-md text-headline-md text-deep-navy">
            Sessão expirada
          </h1>
        </div>
        <p className="font-body-sm text-body-sm text-text-muted">
          Sua sessão expirou. Entre novamente para continuar de onde parou — o que você preencheu
          no formulário não será perdido.
        </p>
        <form onSubmit={handleSubmit(entrar)} noValidate className="space-y-space-md">
          {mensagem && (
            <p
              role="alert"
              className="rounded-lg bg-error-container px-3 py-2 font-body-sm text-body-sm text-on-error-container"
            >
              {mensagem}
            </p>
          )}
          <div className="space-y-1">
            <label htmlFor="reauth-usuario" className="font-label-md text-label-md text-deep-navy">
              Usuário
            </label>
            <input
              id="reauth-usuario"
              autoFocus
              className={classeCampo}
              {...register('usuario', { required: true })}
            />
          </div>
          <div className="space-y-1">
            <label htmlFor="reauth-senha" className="font-label-md text-label-md text-deep-navy">
              Senha
            </label>
            <input
              id="reauth-senha"
              type="password"
              className={classeCampo}
              {...register('senha', { required: true })}
            />
          </div>
          <div className="flex gap-space-sm">
            <button
              type="submit"
              disabled={!isValid || isSubmitting}
              className="flex-1 rounded-lg bg-ocean-blue py-2.5 font-label-md text-label-md font-semibold text-pure-white transition-colors hover:bg-deep-navy disabled:opacity-50"
            >
              Entrar
            </button>
            <button
              type="button"
              onClick={aoCancelar}
              className="rounded-lg bg-surface-container-low px-4 py-2.5 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container"
            >
              Voltar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export function ReauthModal() {
  const aberto = useAuthModalStore((estado) => estado.aberto);
  const concluir = useAuthModalStore((estado) => estado.concluir);
  const cancelar = useAuthModalStore((estado) => estado.cancelar);

  if (!aberto) {
    return null;
  }
  return <ReauthForm aoConcluir={concluir} aoCancelar={cancelar} />;
}
