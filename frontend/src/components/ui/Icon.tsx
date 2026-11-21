interface IconProps {
  nome: string;
  className?: string;
  preenchido?: boolean;
}

/** Ícone Material Symbols Outlined (decorativo — oculto para leitores de tela). */
export function Icon({ nome, className = '', preenchido = false }: IconProps) {
  return (
    <span
      aria-hidden="true"
      className={`material-symbols-outlined ${preenchido ? 'material-symbols-filled' : ''} ${className}`}
    >
      {nome}
    </span>
  );
}
