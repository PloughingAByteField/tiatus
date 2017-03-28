import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { Subscription } from 'rxjs/Subscription';

import { Entry } from '../../../entries/entry.model';
import { AdminEntriesService } from '../entries.service';

@Component({
  selector: 'entries-table',
  styleUrls: [ './entries-table.component.css' ],
  templateUrl: './entries-table.component.html'
})
export class EntriesTableComponent implements OnInit, OnDestroy {

  public entries: Entry[];
  public page: number = 1;
  public itemsPerPage: number = 10;

  private entriesSubscription: Subscription;

  constructor(private entriesService: AdminEntriesService) {}

  public ngOnInit() {
    console.log('hello from entries');
    this.entriesSubscription = this.entriesService.getEntries()
      .subscribe((entries: Entry[]) => this.entries = entries);
  }

  public ngOnDestroy() {
    this.entriesSubscription.unsubscribe();
  }

  public removeEntry(entry: Entry): void {
    console.log('Remove ' + entry.id);
  }
}
