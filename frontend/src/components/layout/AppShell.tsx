import { useEffect } from 'react';
import { Outlet } from 'react-router';
import { registrarReautenticador } from '../../lib/api';
import { useAuthModalStore } from '../../stores/useAuthModalStore';
import { Footer } from './Footer';
import { HeaderNavbar } from './HeaderNavbar';
import { ReauthModal } from './ReauthModal';
import { ToastContainer } from './ToastContainer';

export function AppShell() {
  useEffect(
    () => registrarReautenticador(() => useAuthModalStore.getState().solicitar()),
    [],
  );

  return (
    <div className="flex min-h-screen flex-col bg-surface font-body-md text-on-surface antialiased">
      <HeaderNavbar />
      <main className="min-h-[calc(100vh-16rem)] w-full flex-1 bg-surface pt-16">
        <Outlet />
      </main>
      <Footer />
      <ToastContainer />
      <ReauthModal />
    </div>
  );
}
