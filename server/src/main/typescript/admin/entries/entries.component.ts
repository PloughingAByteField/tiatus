import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'entries',
  styleUrls: [ './entries.component.css' ],
  templateUrl: './entries.component.html'
})
export class EntriesComponent implements OnInit {

  public ngOnInit() {
    console.log('hello from entries');
  }

}
