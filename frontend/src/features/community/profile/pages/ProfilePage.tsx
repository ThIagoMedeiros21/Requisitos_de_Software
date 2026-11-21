import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router';
import { CommunitySubNav } from '../../../../components/layout/CommunitySubNav';
import { consultaPerfilVisao } from '../api/profileQueries';
import { EditProfileModal } from '../components/EditProfileModal';
import { FavoriteCardsSection } from '../components/FavoriteCardsSection';
import { ProfileDecksPreview } from '../components/ProfileDecksPreview';
import { ProfileHeaderCard } from '../components/ProfileHeaderCard';
import { ProfileWishlistPreview } from '../components/ProfileWishlistPreview';
import { SelectFavoriteCardModal } from '../components/SelectFavoriteCardModal';
import { useProfileUiStore } from '../stores/useProfileUiStore';

export function ProfilePage() {
  const { id } = useParams();
  const { pathname } = useLocation();
  const navegar = useNavigate();
  const rotaAlteracaoDedicada = pathname.endsWith('/alteracao');

  const modalEdicaoAberto = useProfileUiStore((estado) => estado.modalEdicaoAberto);
  const definirAbaAtiva = useProfileUiStore((estado) => estado.definirAbaAtiva);

  useEffect(() => {
    definirAbaAtiva('meu-perfil');
  }, [definirAbaAtiva]);

  const { data: perfil } = useSuspenseQuery(consultaPerfilVisao(id));

  if (rotaAlteracaoDedicada) {
    return (
      <div className="mx-auto max-w-7xl space-y-space-xl px-margin py-space-lg md:px-margin-lg">
        <CommunitySubNav abaAtiva="meu-perfil" />
        <EditProfileModal
          perfil={perfil}
          aberto
          emPagina
          aoFechar={() => navegar(`/comunidade/perfil/${encodeURIComponent(perfil.id)}`)}
        />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-7xl space-y-space-xl px-margin py-space-lg md:px-margin-lg">
      <CommunitySubNav abaAtiva="meu-perfil" linkVoltarAmigos={!perfil.ehProprietario} />

      <div id="tab-content-perfil" className="space-y-space-xl">
        <ProfileHeaderCard perfil={perfil} />

        <FavoriteCardsSection perfil={perfil} />

        <div className="grid grid-cols-1 gap-space-md md:grid-cols-2">
          <ProfileDecksPreview perfil={perfil} />
          <ProfileWishlistPreview perfil={perfil} />
        </div>
      </div>

      {perfil.ehProprietario && (
        <>
          <EditProfileModal perfil={perfil} aberto={modalEdicaoAberto} />
          <SelectFavoriteCardModal perfil={perfil} />
        </>
      )}
    </div>
  );
}
