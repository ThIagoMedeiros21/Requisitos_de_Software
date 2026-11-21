import { useState } from 'react';
import { Icon } from '../../../../components/ui/Icon';

interface OpeningCoverProps {
  numero: number;
  nome: string;
  imagem?: string | null;
  className?: string;
}

export function OpeningCover({
  numero,
  nome,
  imagem,
  className = 'aspect-video w-full',
}: OpeningCoverProps) {
  const [falhou, definirFalhou] = useState(false);
  const codigoOp = `OP ${String(numero).padStart(2, '0')}`;

  if (imagem?.trim() && !falhou) {
    return (
      <div className={`overflow-hidden bg-deep-navy ${className}`}>
        <img
          src={imagem}
          alt={`Capa da abertura ${nome}`}
          onError={() => definirFalhou(true)}
          className="h-full w-full object-cover"
        />
      </div>
    );
  }

  return (
    <div
      className={`flex flex-col items-center justify-center bg-gradient-to-br from-deep-navy via-primary to-ocean-blue p-3 text-center text-pure-white ${className}`}
    >
      <Icon nome="library_music" className="text-[28px] text-sky-tint" />
      <span className="mt-1 font-label-sm text-[11px] font-bold tracking-wider text-sky-tint uppercase">
        {codigoOp}
      </span>
      <span className="line-clamp-1 font-headline-sm text-xs text-pure-white">{nome}</span>
    </div>
  );
}
