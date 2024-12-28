import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { HospitalService, Hospital } from '../../services/hospital.service';

@Component({
    selector: 'app-hospital-list',
    standalone: true,
    imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatInputModule, FormsModule],
    template: `
        <h2>Hospitals</h2>
        <table mat-table [dataSource]="hospitals" class="mat-elevation-z8">
            <ng-container matColumnDef="id">
                <th mat-header-cell *matHeaderCellDef>ID</th>
                <td mat-cell *matCellDef="let hospital">{{ hospital.id }}</td>
            </ng-container>
            <ng-container matColumnDef="name">
                <th mat-header-cell *matHeaderCellDef>Name</th>
                <td mat-cell *matCellDef="let hospital">
                    <ng-container *ngIf="editingHospitalId === hospital.id; else nameView">
                        <input matInput [(ngModel)]="hospital.name" />
                    </ng-container>
                    <ng-template #nameView>{{ hospital.name }}</ng-template>
                </td>
            </ng-container>
            <ng-container matColumnDef="address">
                <th mat-header-cell *matHeaderCellDef>Address</th>
                <td mat-cell *matCellDef="let hospital">
                    <ng-container *ngIf="editingHospitalId === hospital.id; else addressView">
                        <input matInput [(ngModel)]="hospital.address" />
                    </ng-container>
                    <ng-template #addressView>{{ hospital.address }}</ng-template>
                </td>
            </ng-container>
            <ng-container matColumnDef="phone">
                <th mat-header-cell *matHeaderCellDef>Phone</th>
                <td mat-cell *matCellDef="let hospital">
                    <ng-container *ngIf="editingHospitalId === hospital.id; else phoneView">
                        <input matInput [(ngModel)]="hospital.phone" />
                    </ng-container>
                    <ng-template #phoneView>{{ hospital.phone }}</ng-template>
                </td>
            </ng-container>
            <ng-container matColumnDef="email">
                <th mat-header-cell *matHeaderCellDef>Email</th>
                <td mat-cell *matCellDef="let hospital">
                    <ng-container *ngIf="editingHospitalId === hospital.id; else emailView">
                        <input matInput [(ngModel)]="hospital.email" />
                    </ng-container>
                    <ng-template #emailView>{{ hospital.email }}</ng-template>
                </td>
            </ng-container>
            <ng-container matColumnDef="specialties">
                <th mat-header-cell *matHeaderCellDef>Specialties</th>
                <td mat-cell *matCellDef="let hospital">
                   <ng-container *ngIf="editingHospitalId === hospital.id; else specialtiesView">
                     <input matInput [value]="hospital.specialties?.join(', ')" (change)="onSpecialtiesChange($event, hospital)" />
                    </ng-container>
                    <ng-template #specialtiesView>{{ hospital.specialties?.join(', ') }}</ng-template>
                </td>
            </ng-container>
            <ng-container matColumnDef="actions">
                <th mat-header-cell *matHeaderCellDef>Actions</th>
                <td mat-cell *matCellDef="let hospital">
                    <ng-container *ngIf="editingHospitalId === hospital.id; else actionsView">
                        <button mat-button color="primary" (click)="saveHospital(hospital)">Save</button>
                        <button mat-button color="warn" (click)="cancelEdit()">Cancel</button>
                    </ng-container>
                    <ng-template #actionsView>
                        <button mat-icon-button color="primary" (click)="startEdit(hospital)">
                            <mat-icon>edit</mat-icon>
                        </button>
                        <button mat-icon-button color="warn" (click)="deleteHospital(hospital.id)">
                            <mat-icon>delete</mat-icon>
                        </button>
                    </ng-template>
                </td>
            </ng-container>
            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>
    `,
    styles: [
        `
      table {
        width: 100%;
      }
    `,
    ],
})
export class HospitalListComponent implements OnInit {
    hospitals: Hospital[] = [];
    displayedColumns: string[] = ['id', 'name', 'address', 'phone', 'email', 'specialties', 'actions'];

    editingHospitalId: number | null = null;
    originalHospital: Hospital | null = null;

    constructor(private hospitalService: HospitalService) { }

    ngOnInit(): void {
        this.loadHospitals();
    }

    loadHospitals(): void {
        this.hospitalService.getHospitals().subscribe(
            (data) => {
                this.hospitals = data;
            },
            (error) => {
                console.error('Error fetching hospitals:', error);
            }
        );
    }

    startEdit(hospital: Hospital): void {
        this.editingHospitalId = hospital.id;
        this.originalHospital = { ...hospital };
    }

  cancelEdit(): void {
      if (this.originalHospital && this.editingHospitalId !== null) {
        const index = this.hospitals.findIndex(a => a.id === this.editingHospitalId);
        if (index !== -1) {
          this.hospitals[index] = { ...this.originalHospital };
        }
         this.originalHospital = null;
         this.editingHospitalId = null;
      }
      this.editingHospitalId = null;
      this.originalHospital = null;
    }

    saveHospital(hospital: Hospital): void {
      if (typeof hospital.specialties === 'string') {
          hospital.specialties = (hospital.specialties as string).split(',').map((item: string) => item.trim());
        }
         else if (!hospital.specialties) {
            hospital.specialties = [];
        }
        this.hospitalService.updateHospital(hospital.id, hospital).subscribe(
            (updatedHospital) => {
                this.editingHospitalId = null;
                this.originalHospital = null;
                this.loadHospitals();
                console.log('Hospital updated:', updatedHospital);
            },
            (error) => {
                console.error('Error updating hospital:', error);
            }
        );
    }


     onSpecialtiesChange(event: Event, hospital: Hospital): void {
      const element = event.target as HTMLInputElement;
      if (element && typeof element.value === 'string') {
            hospital.specialties = (element.value as string).split(',').map((item: string) => item.trim());
      } else{
           hospital.specialties = [];
      }
    }


    deleteHospital(id: number): void {
        this.hospitalService.deleteHospital(id).subscribe(
            () => {
                this.hospitals = this.hospitals.filter(h => h.id !== id);
            },
            (error) => {
                console.error('Error deleting hospital:', error);
            }
        );
    }
}