import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { Patient } from '../../services/patient.service';
import { PatientService } from '../../services/patient.service';

@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatInputModule, FormsModule],
  template: `
    <h2>Patients</h2>
    <table mat-table [dataSource]="patients" class="mat-elevation-z8">
      <ng-container matColumnDef="id">
        <th mat-header-cell *matHeaderCellDef>ID</th>
        <td mat-cell *matCellDef="let patient">{{ patient.id }}</td>
      </ng-container>
      <ng-container matColumnDef="firstName">
        <th mat-header-cell *matHeaderCellDef>First Name</th>
        <td mat-cell *matCellDef="let patient">
          <ng-container *ngIf="editingPatientId === patient.id; else firstNameView">
            <input matInput [(ngModel)]="patient.firstName" />
          </ng-container>
          <ng-template #firstNameView>{{ patient.firstName }}</ng-template>
        </td>
      </ng-container>
      <ng-container matColumnDef="lastName">
        <th mat-header-cell *matHeaderCellDef>Last Name</th>
        <td mat-cell *matCellDef="let patient">
          <ng-container *ngIf="editingPatientId === patient.id; else lastNameView">
            <input matInput [(ngModel)]="patient.lastName" />
          </ng-container>
          <ng-template #lastNameView>{{ patient.lastName }}</ng-template>
        </td>
      </ng-container>
      <ng-container matColumnDef="gender">
        <th mat-header-cell *matHeaderCellDef>Gender</th>
        <td mat-cell *matCellDef="let patient">
          <ng-container *ngIf="editingPatientId === patient.id; else genderView">
            <input matInput [(ngModel)]="patient.gender" />
          </ng-container>
          <ng-template #genderView>{{ patient.gender }}</ng-template>
        </td>
      </ng-container>
      <ng-container matColumnDef="dateOfBirth">
        <th mat-header-cell *matHeaderCellDef>Date of Birth</th>
        <td mat-cell *matCellDef="let patient">
          <ng-container *ngIf="editingPatientId === patient.id; else dateOfBirthView">
            <input matInput [(ngModel)]="patient.dateOfBirth" type="date" />
          </ng-container>
          <ng-template #dateOfBirthView>{{ patient.dateOfBirth | date }}</ng-template>
        </td>
      </ng-container>
      <ng-container matColumnDef="phone">
        <th mat-header-cell *matHeaderCellDef>Phone</th>
        <td mat-cell *matCellDef="let patient">
          <ng-container *ngIf="editingPatientId === patient.id; else phoneView">
            <input matInput [(ngModel)]="patient.phone" />
          </ng-container>
          <ng-template #phoneView>{{ patient.phone }}</ng-template>
        </td>
      </ng-container>
      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef>Actions</th>
        <td mat-cell *matCellDef="let patient">
          <ng-container *ngIf="editingPatientId === patient.id; else actionsView">
            <button mat-button color="primary" (click)="savePatient(patient)">Save</button>
            <button mat-button color="warn" (click)="cancelEdit()">Cancel</button>
          </ng-container>
          <ng-template #actionsView>
            <button mat-icon-button color="primary" (click)="startEdit(patient)">
              <mat-icon>edit</mat-icon>
            </button>
            <button mat-icon-button color="warn" (click)="deletePatient(patient.id)">
              <mat-icon>delete</mat-icon>
            </button>
          </ng-template>
        </td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
  `,
  styles: [`
    table {
      width: 100%;
    }
  `]
})
export class PatientListComponent implements OnInit {
  patients: Patient[] = [];
  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'gender', 'dateOfBirth', 'phone', 'actions'];

  editingPatientId: number | null = null;
  originalPatient: Patient | null = null;

  constructor(private patientService: PatientService) {}

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    this.patientService.getPatients().subscribe(
      (data) => {
        this.patients = data;
      },
      (error) => {
        console.error('Error fetching patients:', error);
      }
    );
  }

  startEdit(patient: Patient): void {
    this.editingPatientId = patient.id;
    this.originalPatient = { ...patient };
  }

  cancelEdit(): void {
    if (this.editingPatientId !== null && this.originalPatient) {
      const index = this.patients.findIndex(p => p.id === this.editingPatientId);
      if (index > -1) {
        this.patients[index] = { ...this.originalPatient };
      }
    }
    this.editingPatientId = null;
    this.originalPatient = null;
  }

  savePatient(patient: Patient): void {
    this.patientService.updatePatient(patient.id, patient).subscribe(
      (updatedPatient) => {
        this.editingPatientId = null;
        this.originalPatient = null;
        console.log('Patient updated:', updatedPatient);
      },
      (error) => {
        console.error('Error updating patient:', error);
      }
    );
  }

  deletePatient(id: number): void {
    this.patientService.deletePatient(id).subscribe(
      () => {
        this.patients = this.patients.filter(p => p.id !== id);
      },
      (error) => {
        console.error('Error deleting patient:', error);
      }
    );
  }
}
