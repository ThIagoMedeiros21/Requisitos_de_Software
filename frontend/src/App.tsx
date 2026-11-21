import { RouterProvider } from 'react-router/dom';
import { roteador } from './routes';

export function App() {
  return <RouterProvider router={roteador} />;
}
