import { Link } from 'react-router';
import { type AbaComunidade, usePerfilStore } from './perfilStore';

interface ItemAba {
  chave: AbaComunidade;
  rotulo: string;
  caminho: string;
}

const ABAS_F001: ItemAba[] = [
  {
    chave: 'meu-perfil',
    rotulo: 'Meu Perfil',
    caminho: '/comunidade/perfil-comunitario/visualizacao',
  },
  {
    chave: 'rede-amigos',
    rotulo: 'Rede de Amigos',
    caminho: '/comunidade/amizade/pesquisa',
  },
  {
    chave: 'buscar-colecionadores',
    rotulo: 'Buscar Colecionadores',
    caminho: '/comunidade/amizade/inclusao',
  },
];

export function BarraNavegacaoComunidade({ abaAtual }: { abaAtual: AbaComunidade }) {
  const definirAbaAtiva = usePerfilStore((estado) => estado.definirAbaAtiva);

  return (
    <nav
      aria-label="Navegação da comunidade"
      className="mb-6 flex flex-wrap items-center gap-2 rounded-xl border border-slate-200 bg-white p-2 shadow-xs"
    >
      {ABAS_F001.map((aba) => {
        const ativa = aba.chave === abaAtual;
        return (
          <Link
            key={aba.chave}
            to={aba.caminho}
            onClick={() => definirAbaAtiva(aba.chave)}
            className={`rounded-lg px-4 py-2 text-sm font-medium no-underline transition ${
              ativa
                ? 'bg-slate-900 text-white shadow-xs'
                : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900'
            }`}
          >
            {aba.rotulo}
          </Link>
        );
      })}
    </nav>
  );
}
