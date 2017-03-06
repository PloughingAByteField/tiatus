import { Component, OnInit } from '@angular/core';

import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'landing',
  styleUrls: [ './landing.component.css' ],
  templateUrl: './landing.component.html'
})
export class LandingComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from landing');
  }

}
