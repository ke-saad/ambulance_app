import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface NearestAmbulance {
  id: number;
  registrationNumber: string;
  model: string;
  status: string;
  latitude: number;
  longitude: number;
  distance: number;
  phone: string;
}

@Injectable({
  providedIn: 'root'
})
export class RoutingService {
  private apiUrl = 'http://localhost:8080/routings';

  constructor(private http: HttpClient) { }

  findNearestAmbulance(patientLatitude: number, patientLongitude: number, specialty?: string): Observable<NearestAmbulance> {
    let url = `${this.apiUrl}/nearest-ambulance?patientLatitude=${patientLatitude}&patientLongitude=${patientLongitude}`;
    if (specialty) {
      url += `&specialty=${specialty}`;
    }
    return this.http.get<NearestAmbulance>(url);
  }
}

