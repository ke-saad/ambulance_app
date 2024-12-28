import { Routes } from '@angular/router';
import { AmbulanceListComponent } from './components/ambulance-list/ambulance-list.component';
import { HospitalListComponent } from './components/hospital-list/hospital-list.component';
import { PatientListComponent } from './components/patient-list/patient-list.component';
import { RoutingDashboardComponent } from './components/routing-dashboard/routing-dashboard.component';

export const routes: Routes = [
    { path: 'ambulances', component: AmbulanceListComponent },
    { path: 'hospitals', component: HospitalListComponent },
    { path: 'patients', component: PatientListComponent },
    { path: 'routings', component: RoutingDashboardComponent },
  
    { path: '', redirectTo: '/ambulances', pathMatch: 'full' },
  ];
