import { Icon } from '../ui/Icon';

const COLUNAS = [
  { titulo: 'Marketplace', itens: ['Catálogo', 'Decks', 'Wishlist'] },
  { titulo: 'Comunidade', itens: ['Meu Perfil', 'Rede de Amigos', 'Buscar Colecionadores'] },
  { titulo: 'Institucional', itens: ['Sobre o TCGHub', 'Termos de Uso', 'Privacidade'] },
];

export function Footer() {
  return (
    <footer className="w-full border-t border-ice-border bg-pure-white">
      <div className="mx-auto grid max-w-7xl gap-space-xl px-margin py-space-xl md:grid-cols-4 md:px-margin-lg">
        <div className="space-y-space-sm">
          <div className="flex items-center gap-space-xs font-headline-md text-headline-md tracking-tight text-primary">
            <Icon nome="anchor" className="text-[24px] text-ocean-blue" />
            <span>
              TCG<span className="font-extrabold text-ocean-blue">Hub</span>
            </span>
          </div>
          <p className="font-body-sm text-body-sm text-text-muted">
            Marketplace e comunidade de One Piece Card Game.
          </p>
        </div>
        {COLUNAS.map((coluna) => (
          <div key={coluna.titulo} className="space-y-space-sm">
            <h2 className="font-label-sm text-label-sm font-bold uppercase tracking-wider text-secondary">
              {coluna.titulo}
            </h2>
            <ul className="m-0 list-none space-y-1 p-0">
              {coluna.itens.map((item) => (
                <li key={item} className="font-body-sm text-body-sm text-text-muted">
                  {item}
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>
      <div className="border-t border-ice-border py-space-md text-center font-body-sm text-body-sm text-text-muted">
        © {new Date().getFullYear()} TCGHub. Todos os direitos reservados.
      </div>
    </footer>
  );
}
