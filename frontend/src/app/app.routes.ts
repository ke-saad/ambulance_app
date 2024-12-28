import { Routes } from '@angular/router';
import { AmbulanceListComponent } from './components/ambulance-list/ambulance-list.component';
import { AmbulanceDetailComponent } from './components/ambulance-detail/ambulance-detail.component';
import { AmbulanceFormComponent } from './components/ambulance-form/ambulance-form.component';

export const routes: Routes = [
    { path: '', redirectTo: '/ambulances', pathMatch: 'full' },
    { path: 'ambulances', component: AmbulanceListComponent },
    { path: 'ambulance/create', component: AmbulanceFormComponent },
    { path: 'ambulance/:id', component: AmbulanceDetailComponent },
    { path: 'ambulance/edit/:id', component: AmbulanceFormComponent },
];