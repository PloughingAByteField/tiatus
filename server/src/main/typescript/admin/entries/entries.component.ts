import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'entries',
  styleUrls: [ './entries.component.css' ],
  templateUrl: './entries.component.html'
})
export class EntriesComponent implements OnInit {

  constructor(private router: Router) {}

  public ngOnInit() {
    this.router.navigate(['/entries', 1]);
  }
}
