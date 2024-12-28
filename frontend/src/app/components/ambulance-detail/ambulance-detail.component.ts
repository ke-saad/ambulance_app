import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AmbulanceService } from '../../services/ambulance-service.service';
import { Ambulance } from '../../models/ambulance.model';

@Component({
  selector: 'app-ambulance-detail',
  templateUrl: './ambulance-detail.component.html',
  styleUrls: ['./ambulance-detail.component.css']
})
export class AmbulanceDetailComponent implements OnInit {
  ambulance: Ambulance | null = null;
  loading: boolean = false;
  error: string = '';

  constructor(
    private ambulanceService: AmbulanceService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const ambulanceId = +this.route.snapshot.paramMap.get('id')!;
    if (ambulanceId) {
      this.fetchAmbulanceDetails(ambulanceId);
    }
  }

  fetchAmbulanceDetails(id: number): void {
    this.loading = true;
    this.ambulanceService.getAmbulanceById(id).subscribe({
      next: (data) => {
        this.ambulance = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error fetching ambulance details';
        this.loading = false;
      }
    });
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