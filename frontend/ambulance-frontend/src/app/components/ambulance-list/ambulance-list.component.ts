import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { AmbulanceService, Ambulance } from '../../services/ambulance.service';

// Define the enums to match the backend
enum Specialty {
  Cardiology = 'Cardiology',
  Neurology = 'Neurology',
  Trauma = 'Trauma',
  Pediatrics = 'Pediatrics',
  General = 'General',
  Oncology = 'Oncology',
  Respiratory = 'Respiratory'
}

enum AmbulanceStatus {
  Available = 'Available',
  Busy = 'Busy',
  Dispatched = 'Dispatched',
  Unavailable = 'Unavailable'
}


@Component({
    selector: 'app-ambulance-list',
    standalone: true,
    imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatInputModule, FormsModule, MatSelectModule],
    template: `
    <h2>Ambulances</h2>
    <button mat-raised-button color="primary" (click)="startAddAmbulance()" *ngIf="!addingAmbulance">
        <mat-icon>add</mat-icon>
        Add Ambulance
    </button>

    <div *ngIf="addingAmbulance">
        <h3>Add New Ambulance</h3>
        <mat-form-field>
            <mat-label>Registration Number</mat-label>
            <input matInput [(ngModel)]="newAmbulance.registrationNumber" />
        </mat-form-field>
        <mat-form-field>
            <mat-label>Model</mat-label>
            <input matInput [(ngModel)]="newAmbulance.model" />
        </mat-form-field>
        <mat-form-field>
            <mat-label>Status</mat-label>
              <mat-select [(ngModel)]="newAmbulance.status">
                <mat-option *ngFor="let status of ambulanceStatuses" [value]="status">{{status}}</mat-option>
              </mat-select>
        </mat-form-field>
        <mat-form-field>
             <mat-label>Specialty</mat-label>
            <mat-select [(ngModel)]="newAmbulance.specialty">
                <mat-option *ngFor="let specialty of specialties" [value]="specialty">{{specialty}}</mat-option>
            </mat-select>
        </mat-form-field>
          <mat-form-field>
            <mat-label>Phone</mat-label>
            <input matInput [(ngModel)]="newAmbulance.phone" />
        </mat-form-field>
        <mat-form-field>
            <mat-label>Latitude</mat-label>
            <input matInput type="number" [(ngModel)]="newAmbulance.latitude" />
        </mat-form-field>
        <mat-form-field>
            <mat-label>Longitude</mat-label>
            <input matInput type="number" [(ngModel)]="newAmbulance.longitude" />
        </mat-form-field>
        <button mat-raised-button color="primary" (click)="createAmbulance()">Save</button>
         <button mat-raised-button color="warn" (click)="cancelAddAmbulance()">Cancel</button>

    </div>

    <table mat-table [dataSource]="ambulances" class="mat-elevation-z8" *ngIf="!addingAmbulance">
        <ng-container matColumnDef="id">
            <th mat-header-cell *matHeaderCellDef>ID</th>
            <td mat-cell *matCellDef="let ambulance">{{ambulance.id}}</td>
        </ng-container>
        <ng-container matColumnDef="registrationNumber">
            <th mat-header-cell *matHeaderCellDef>Registration Number</th>
            <td mat-cell *matCellDef="let ambulance">
                <ng-container *ngIf="editingAmbulanceId === ambulance.id; else registrationNumberView">
                    <input matInput [(ngModel)]="ambulance.registrationNumber" />
                </ng-container>
                <ng-template #registrationNumberView>{{ ambulance.registrationNumber }}</ng-template>
            </td>
        </ng-container>
        <ng-container matColumnDef="model">
            <th mat-header-cell *matHeaderCellDef>Model</th>
            <td mat-cell *matCellDef="let ambulance">
                <ng-container *ngIf="editingAmbulanceId === ambulance.id; else modelView">
                    <input matInput [(ngModel)]="ambulance.model" />
                </ng-container>
                <ng-template #modelView>{{ ambulance.model }}</ng-template>
            </td>
        </ng-container>
        <ng-container matColumnDef="status">
            <th mat-header-cell *matHeaderCellDef>Status</th>
            <td mat-cell *matCellDef="let ambulance">
                <ng-container *ngIf="editingAmbulanceId === ambulance.id; else statusView">
                    <input matInput [(ngModel)]="ambulance.status" />
                </ng-container>
                <ng-template #statusView>{{ ambulance.status }}</ng-template>
            </td>
        </ng-container>
        <ng-container matColumnDef="specialty">
            <th mat-header-cell *matHeaderCellDef>Specialty</th>
            <td mat-cell *matCellDef="let ambulance">
                <ng-container *ngIf="editingAmbulanceId === ambulance.id; else specialtyView">
                    <input matInput [(ngModel)]="ambulance.specialty" />
                </ng-container>
                <ng-template #specialtyView>{{ ambulance.specialty }}</ng-template>
            </td>
        </ng-container>
        <ng-container matColumnDef="phone">
            <th mat-header-cell *matHeaderCellDef>Phone</th>
            <td mat-cell *matCellDef="let ambulance">
                <ng-container *ngIf="editingAmbulanceId === ambulance.id; else phoneView">
                    <input matInput [(ngModel)]="ambulance.phone" />
                </ng-container>
                <ng-template #phoneView>{{ ambulance.phone }}</ng-template>
            </td>
        </ng-container>
        <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let ambulance">
                <ng-container *ngIf="editingAmbulanceId === ambulance.id; else actionsView">
                    <button mat-button color="primary" (click)="saveAmbulance(ambulance)">Save</button>
                    <button mat-button color="warn" (click)="cancelEdit()">Cancel</button>
                </ng-container>
                <ng-template #actionsView>
                    <button mat-icon-button color="primary" (click)="editAmbulance(ambulance)">
                        <mat-icon>edit</mat-icon>
                    </button>
                    <button mat-icon-button color="warn" (click)="deleteAmbulance(ambulance.id)">
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
export class AmbulanceListComponent implements OnInit {
    ambulances: Ambulance[] = [];
    displayedColumns: string[] = ['id', 'registrationNumber', 'model', 'status', 'specialty', 'phone', 'actions'];
    editingAmbulanceId: number | null = null;
    originalAmbulance: Ambulance | null = null;
    addingAmbulance: boolean = false;
    newAmbulance: Ambulance = {
        id: 0,
        registrationNumber: '',
        model: '',
        status: '',
        latitude: 0,
        longitude: 0,
        specialty: '',
        phone: ''
    };

    specialties = Object.values(Specialty);
    ambulanceStatuses = Object.values(AmbulanceStatus);


    constructor(private ambulanceService: AmbulanceService) { }

    ngOnInit(): void {
        this.loadAmbulances();
    }

    loadAmbulances(): void {
        this.ambulanceService.getAmbulances().subscribe(
            (data) => {
                this.ambulances = data;
            },
            (error) => {
                console.error('Error fetching ambulances:', error);
            }
        );
    }
      startAddAmbulance(): void {
        this.addingAmbulance = true;
    }
    cancelAddAmbulance(): void {
         this.addingAmbulance = false;
         this.newAmbulance = {
           id: 0,
           registrationNumber: '',
           model: '',
           status: '',
           latitude: 0,
           longitude: 0,
           specialty: '',
           phone: ''
      };
    }
    createAmbulance(): void {
       this.ambulanceService.createAmbulance(this.newAmbulance).subscribe(
            (createdAmbulance) => {
                 this.ambulances.push(createdAmbulance);
                 this.addingAmbulance = false;
                  this.newAmbulance = {
                     id: 0,
                     registrationNumber: '',
                     model: '',
                     status: '',
                     latitude: 0,
                     longitude: 0,
                     specialty: '',
                     phone: ''
                  };
              },
            (error) => {
                console.error('Error creating ambulance:', error);
            }
        );
    }

  editAmbulance(ambulance: Ambulance): void {
      this.editingAmbulanceId = ambulance.id;
      this.originalAmbulance = { ...ambulance };
    }

    saveAmbulance(ambulance: Ambulance): void {
        this.ambulanceService.updateAmbulance(ambulance.id, ambulance).subscribe(
            (updatedAmbulance) => {
                this.editingAmbulanceId = null;
                 this.originalAmbulance = null;
                this.loadAmbulances();
                console.log('Ambulance updated:', updatedAmbulance);
            },
            (error) => {
                console.error('Error updating ambulance:', error);
            }
        );
    }

    cancelEdit(): void {
        if (this.originalAmbulance && this.editingAmbulanceId !== null) {
          const index = this.ambulances.findIndex(a => a.id === this.editingAmbulanceId);
          if (index !== -1) {
            this.ambulances[index] = { ...this.originalAmbulance };
           }
            this.originalAmbulance = null;
           this.editingAmbulanceId = null;

      }
       this.originalAmbulance = null;
       this.editingAmbulanceId = null;
    }

    deleteAmbulance(id: number): void {
        this.ambulanceService.deleteAmbulance(id).subscribe(
            () => {
                this.ambulances = this.ambulances.filter(a => a.id !== id);
            },
            (error) => {
                console.error('Error deleting ambulance:', error);
            }
        );
    }
}