import { Component, OnInit } from '@angular/core';

import { Entry } from '../../entries/entry.model';
import { AdminEntriesService } from './entries.service';

@Component({
  selector: 'entries',
  styleUrls: [ './entries.component.css' ],
  templateUrl: './entries.component.html'
})
export class EntriesComponent implements OnInit {

  public entries: Entry[];

  constructor(private entriesService: AdminEntriesService) {}

  public ngOnInit() {
    console.log('hello from entries');
    this.entriesService.getEntries()
      .subscribe((entries: Entry[]) => this.entries = entries);
  }

}
