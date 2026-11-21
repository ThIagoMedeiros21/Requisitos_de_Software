interface AvatarProps {
  apelido: string;
  foto?: string | null;
  className?: string;
}

function iniciais(apelido: string): string {
  const limpo = apelido.replace(/^@/, '').trim();
  return (limpo.slice(0, 2) || '?').toUpperCase();
}

/** Avatar circular: foto quando houver, senão as iniciais do apelido sobre fundo náutico. */
export function Avatar({ apelido, foto, className = 'w-12 h-12' }: AvatarProps) {
  return (
    <div
      className={`flex flex-shrink-0 items-center justify-center overflow-hidden rounded-full bg-ocean-blue font-headline-sm text-headline-sm text-pure-white shadow-sm ${className}`}
    >
      {foto ? (
        <img src={foto} alt={`Foto de ${apelido}`} className="h-full w-full object-cover" />
      ) : (
        <span aria-hidden="true">{iniciais(apelido)}</span>
      )}
    </div>
  );
}
