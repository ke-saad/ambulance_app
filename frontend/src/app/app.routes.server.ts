import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '',
    renderMode: RenderMode.Prerender,
  },
  {
    path: 'ambulances',
    renderMode: RenderMode.Prerender,
  },
  { path: 'ambulance/create', renderMode: RenderMode.Prerender },
  { path: 'ambulance/:id', renderMode: RenderMode.Prerender },
  { path: 'ambulance/edit/:id', renderMode: RenderMode.Prerender },
];
