import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'config',
  styleUrls: [ './config.component.css' ],
  templateUrl: './config.component.html'
})
export class ConfigComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from config');
  }

}
