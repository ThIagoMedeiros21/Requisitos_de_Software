import { QueryClientProvider } from '@tanstack/react-query';
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { App } from './App';
import { clienteConsultas } from './lib/queryClient';
import './styles/globals.css';

createRoot(document.getElementById('raiz')!).render(
  <StrictMode>
    <QueryClientProvider client={clienteConsultas}>
      <App />
    </QueryClientProvider>
  </StrictMode>,
);
