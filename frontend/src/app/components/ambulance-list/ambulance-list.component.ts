import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AmbulanceService } from '../../services/ambulance-service.service';
import { Ambulance } from '../../models/ambulance.model';

@Component({
  selector: 'app-ambulance-list',
  templateUrl: './ambulance-list.component.html',
  styleUrls: ['./ambulance-list.component.css']
})
export class AmbulanceListComponent implements OnInit {
  ambulances:  Ambulance[] = [];
  loading = false;
  error: string | null = null;

  constructor(private router: Router, private ambulanceService: AmbulanceService) {}

  ngOnInit() {
    this.loading = true;
    this.ambulanceService.getAmbulances().subscribe(
      (data: any) => {
        this.ambulances = data;
        this.loading = false;
      },
      (err: any) => {
        this.error = err.message;
        this.loading = false;
      }
    );
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
