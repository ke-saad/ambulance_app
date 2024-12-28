import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AmbulanceService } from '../../services/ambulance-service.service';
import { Ambulance } from '../../models/ambulance.model';
import { ReactiveFormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-ambulance-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './ambulance-form.component.html',
  styleUrls: ['./ambulance-form.component.css']
})
export class AmbulanceFormComponent implements OnInit {
  ambulanceForm: FormGroup;
  loading: boolean = false;
  error: string = '';
  ambulanceId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private ambulanceService: AmbulanceService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.ambulanceForm = this.fb.group({
      registrationNumber: ['', Validators.required],
      model: ['', Validators.required],
      status: ['', Validators.required],
      specialty: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]]
    });
  }

  ngOnInit(): void {
    this.ambulanceId = +this.route.snapshot.paramMap.get('id')!;
    if (this.ambulanceId) {
      this.fetchAmbulanceDetails(this.ambulanceId);
    }
  }

  fetchAmbulanceDetails(id: number): void {
    this.loading = true;
    this.ambulanceService.getAmbulanceById(id).subscribe({
      next: (data) => {
        this.ambulanceForm.patchValue(data);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error fetching ambulance details';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.ambulanceForm.invalid) {
      return;
    }

    this.loading = true;
    const ambulance: Ambulance = this.ambulanceForm.value;

    if (this.ambulanceId) {
      this.ambulanceService.updateAmbulance(this.ambulanceId, ambulance).subscribe({
        next: () => {
          this.router.navigate(['/ambulances']);
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error updating ambulance';
          this.loading = false;
        }
      });
    } else {
      this.ambulanceService.createAmbulance(ambulance).subscribe({
        next: () => {
          this.router.navigate(['/ambulances']);
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error creating ambulance';
          this.loading = false;
        }
      });
    }
  }

  navigateToList() {
    this.router.navigate(['/ambulances']);
  }

  navigateToDetails(id: number) {
    this.router.navigate(['/ambulance', id]);  // Navigate to ambulance detail page
  }

  navigateToEditForm(id: number) {
    this.router.navigate(['/ambulance/edit', id]);  // Navigate to ambulance edit form
  }

  navigateToCreateForm() {
    this.router.navigate(['/ambulance/create']);  // Navigate to create new ambulance form
  }
}
