import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ambulance } from '../models/ambulance.model';

@Injectable({
  providedIn: 'root'
})
export class AmbulanceService {

  private apiUrl = 'http://localhost:8080/ambulances';

  constructor(private http: HttpClient) { }

  // Get all ambulances
  getAmbulances(): Observable<Ambulance[]> {
    return this.http.get<Ambulance[]>(this.apiUrl);
  }

  // Get ambulance by ID
  getAmbulanceById(id: number): Observable<Ambulance> {
    return this.http.get<Ambulance>(`${this.apiUrl}/${id}`);
  }

  // Create a new ambulance
  createAmbulance(ambulance: Ambulance): Observable<Ambulance> {
    return this.http.post<Ambulance>(this.apiUrl, ambulance);
  }

  // Update an existing ambulance
  updateAmbulance(id: number, ambulance: Ambulance): Observable<Ambulance> {
    return this.http.put<Ambulance>(`${this.apiUrl}/${id}`, ambulance);
  }

  // Update ambulance location
  updateAmbulanceLocation(id: number, latitude: number, longitude: number): Observable<Ambulance> {
    return this.http.put<Ambulance>(`${this.apiUrl}/${id}/location?latitude=${latitude}&longitude=${longitude}`, {});
  }

  // Delete an ambulance by ID
  deleteAmbulance(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
