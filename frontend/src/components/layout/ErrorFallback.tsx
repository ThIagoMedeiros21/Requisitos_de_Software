import { isRouteErrorResponse, Link, useRouteError } from 'react-router';
import { mensagemErro } from '../../lib/api';
import { Icon } from '../ui/Icon';

/**
 * Exibe, no lugar da rota, o erro lançado por seu loader (por exemplo, um 404 do backend para um
 * identificador inexistente) ou o de um caminho que não corresponde a nenhuma rota.
 */
export function ErrorFallback() {
  const erro = useRouteError();
  const mensagem =
    isRouteErrorResponse(erro) && erro.status === 404 ? 'Página não encontrada.' : mensagemErro(erro);

  return (
    <div className="mx-auto max-w-2xl px-margin py-space-xxl">
      <div
        role="alert"
        className="flex items-start gap-space-md rounded-xl border border-ice-border bg-pure-white p-space-lg shadow-level-1"
      >
        <span className="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-xl bg-error-container text-error">
          <Icon nome="sailing" className="text-[28px]" />
        </span>
        <div className="space-y-space-sm">
          <h1 className="font-headline-md text-headline-md text-deep-navy">Algo saiu do curso</h1>
          <p className="font-body-md text-body-md text-text-muted">{mensagem}</p>
          <Link
            to="/"
            className="inline-flex items-center gap-1 font-label-md text-label-md font-semibold text-ocean-blue hover:text-deep-navy"
          >
            <Icon nome="arrow_back" className="text-[18px]" />
            Voltar ao início
          </Link>
        </div>
      </div>
    </div>
  );
}
