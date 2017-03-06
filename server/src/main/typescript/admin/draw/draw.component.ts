import { Component, OnInit } from '@angular/core';

import { AdminDrawService } from './draw.service';

@Component({
  selector: 'draw',
  styleUrls: [ './draw.component.css' ],
  templateUrl: './draw.component.html'
})
export class DrawComponent implements OnInit {
  constructor(private drawService: AdminDrawService) {}

  public ngOnInit() {
    console.log('hello from draw');
  }

}
