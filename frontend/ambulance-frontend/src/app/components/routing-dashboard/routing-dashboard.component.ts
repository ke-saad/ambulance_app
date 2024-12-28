import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { RoutingService, NearestAmbulance } from '../../services/routing.service';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-routing-dashboard',
  standalone: true,
  imports: [RouterModule,CommonModule, FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatCardModule],
  template: `
    <h2>Find Nearest Ambulance</h2>
    <form (ngSubmit)="findNearestAmbulance()">
      <mat-form-field>
        <mat-label>Patient Latitude</mat-label>
        <input matInput type="number" [(ngModel)]="patientLatitude" name="patientLatitude" required>
      </mat-form-field>
      <mat-form-field>
        <mat-label>Patient Longitude</mat-label>
        <input matInput type="number" [(ngModel)]="patientLongitude" name="patientLongitude" required>
      </mat-form-field>
      <mat-form-field>
        <mat-label>Specialty (optional)</mat-label>
        <input matInput [(ngModel)]="specialty" name="specialty">
      </mat-form-field>
      <button mat-raised-button color="primary" type="submit">Find Nearest Ambulance</button>
    </form>

    <mat-card *ngIf="nearestAmbulance">
      <mat-card-header>
        <mat-card-title>Nearest Ambulance</mat-card-title>
      </mat-card-header>
      <mat-card-content>
        <p>ID: {{nearestAmbulance.id}}</p>
        <p>Registration Number: {{nearestAmbulance.registrationNumber}}</p>
        <p>Model: {{nearestAmbulance.model}}</p>
        <p>Status: {{nearestAmbulance.status}}</p>
        <p>Distance: {{nearestAmbulance.distance}} km</p>
        <p>Phone: {{nearestAmbulance.phone}}</p>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    form {
      display: flex;
      flex-direction: column;
      max-width: 300px;
      margin-bottom: 20px;
    }
    mat-card {
      max-width: 400px;
    }
  `]
})
export class RoutingDashboardComponent {
  patientLatitude: number | null = null;
  patientLongitude: number | null = null;
  specialty: string = '';
  nearestAmbulance: NearestAmbulance | null = null;

  constructor(private routingService: RoutingService) { }

  findNearestAmbulance(): void {
    if (this.patientLatitude !== null && this.patientLongitude !== null) {
      this.routingService.findNearestAmbulance(this.patientLatitude, this.patientLongitude, this.specialty).subscribe(
        (data) => {
          this.nearestAmbulance = data;
        },
        (error) => {
          console.error('Error finding nearest ambulance:', error);
        }
      );
    }
  }
}

