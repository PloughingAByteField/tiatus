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
export class TimesService {

  private observable: Observable<EntryTime[]>;
  private raceEntryTimes: RaceEntryTimes[] = new Array<RaceEntryTimes>();
  private timeEndPoint: string = '/rest/time/';
  private raceEndPoint: string = 'race/';

  constructor(private http: Http) {}

  public getTimesForRace(race: Race): Observable<EntryTime[]> {
    if (race) {
      for (let raceEntryTime of this.raceEntryTimes) {
        if (raceEntryTime.race.id === race.id) {
          console.log('match');
          return Observable.of(raceEntryTime.entries);
        }
      }
    }

    if (this.observable) {
      return this.observable;

    } else {
      if (race) {
        console.log('race not fetched');
        return this.http.get(this.timeEndPoint + this.raceEndPoint + race.id)
          .map((response) => {
            console.log('fetched');
            let data = response.json();
            this.observable = null;
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
              console.log(entryTimeForEntry.times);
            });
            this.raceEntryTimes.push(rpt);
            return rpt.entries;

          }).share();
      }
    }

    return this.observable;
  }

}
