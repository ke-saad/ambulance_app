import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Hospital {
  id: number;
  name: string;
  address: string;
  phone: string;
  email: string;
  latitude: number;
  longitude: number;
  specialties: string[];
}

@Injectable({
  providedIn: 'root'
})
export class HospitalService {
  private apiUrl = 'http://localhost:8080/hospitals';

  constructor(private http: HttpClient) { }

  getHospitals(): Observable<Hospital[]> {
    return this.http.get<Hospital[]>(this.apiUrl);
  }

  getHospital(id: number): Observable<Hospital> {
    return this.http.get<Hospital>(`${this.apiUrl}/${id}`);
  }

  createHospital(hospital: Hospital): Observable<Hospital> {
    return this.http.post<Hospital>(this.apiUrl, hospital);
  }

  updateHospital(id: number, hospital: Hospital): Observable<Hospital> {
    return this.http.put<Hospital>(`${this.apiUrl}/${id}`, hospital);
  }

  deleteHospital(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

