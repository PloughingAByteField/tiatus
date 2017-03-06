import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'draw',
  styleUrls: [ './draw.component.css' ],
  templateUrl: './draw.component.html'
})
export class DrawComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from draw');
  }

}
