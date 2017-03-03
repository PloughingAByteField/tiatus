import { EventEmitter, Injectable } from '@angular/core';
import { Headers, Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/toPromise';

import { EntryTime } from '../models/entry-time.model';
import { PositionTime, convertJsonToPositionTime } from '../models/postion-time.model';
import { RaceEntryTimes } from '../models/race-entry-times.model';
import { Race } from '../races/race.model';
import { Entry } from '../entries/entry.model';
import { Position } from '../models/position.model';

@Injectable()
export class TimesHttpService {

  private timeEndPoint: string = '/rest/time/race/';
  private raceEndPoint: string = 'race/';

  constructor(private http: Http) {}

  public getTimesForRace(race: Race): Observable<EntryTime[]> {
      if (race) {
        return this.http.get(this.timeEndPoint + race.id)
          .map((response) => {
            let data = response.json();
            let rpt: RaceEntryTimes = new RaceEntryTimes();
            rpt.race = race;
            rpt.entries = new Array<EntryTime>();
            data.filter((positionTime: PositionTime) => {
              let entryTimesForEntry: EntryTime[] = rpt.entries
                .filter((entryTime: EntryTime) => entryTime.entry.id === positionTime.entry);
              let entryTimeForEntry: EntryTime;
              if (entryTimesForEntry.length === 0) {
                  entryTimeForEntry = new EntryTime();
                  entryTimeForEntry.entry = new Entry();
                  entryTimeForEntry.entry.id = positionTime.entry;
                  entryTimeForEntry.times = new Array<PositionTime>();
                  rpt.entries.push(entryTimeForEntry);
              } else {
                entryTimeForEntry = entryTimesForEntry[0];
              }
              entryTimeForEntry.times.push(convertJsonToPositionTime(positionTime));
            });
            return rpt.entries;

          }).share();
      }
    }
}
