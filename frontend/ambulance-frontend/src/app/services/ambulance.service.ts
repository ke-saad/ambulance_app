import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Ambulance {
  id: number;
  registrationNumber: string;
  model: string;
  status: string;
  latitude: number;
  longitude: number;
  specialty: string;
  phone: string;
}

@Injectable({
  providedIn: 'root'
})
export class AmbulanceService {
  private apiUrl = 'http://localhost:8082/ambulances';

  constructor(private http: HttpClient) { }

  getAmbulances(): Observable<Ambulance[]> {
    return this.http.get<Ambulance[]>(this.apiUrl);
  }

  getAmbulance(id: number): Observable<Ambulance> {
    return this.http.get<Ambulance>(`${this.apiUrl}/${id}`);
  }

  createAmbulance(ambulance: Ambulance): Observable<Ambulance> {
    return this.http.post<Ambulance>(this.apiUrl, ambulance);
  }

  updateAmbulance(id: number, ambulance: Ambulance): Observable<Ambulance> {
    return this.http.put<Ambulance>(`${this.apiUrl}/${id}`, ambulance);
  }

  deleteAmbulance(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

