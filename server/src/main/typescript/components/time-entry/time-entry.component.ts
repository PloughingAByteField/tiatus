import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'time-entry',
  styleUrls: [ './time-entry.component.css' ],
  templateUrl: './time-entry.component.html',
})
export class TimeEntryComponent implements OnInit {
  @Input()
  public time: number;
  @Input()
  public displayMillSeconds: boolean;
  @Output()
  public updatedTime: EventEmitter<number> = new EventEmitter<number>();

  public hours: string = null;
  public minutes: string = null;
  public seconds: string = null;
  public milliSeconds: string = null;

  public ngOnInit() {
    if (this.time !== 0) {
      this.fillFieldsForTime(this.time);
    }
  }

  public onChangeHour(value: number): void {
    if (!isNaN(value)) {
      this.updateTime(value, 'hours');
    } else {
      this.fillFieldsForTime(this.time);
    }
  }

  public onChangeMinute(value: number): void {
    if (!isNaN(value)) {
      this.updateTime(value, 'minutes');
    } else {
      this.fillFieldsForTime(this.time);
    }
  }

  public onChangeSecond(value: number): void {
    if (!isNaN(value)) {
      this.updateTime(value, 'seconds');
    } else {
      this.fillFieldsForTime(this.time);
    }
  }

  public onChangeMilliSecond(value: number): void {
    if (!isNaN(value)) {
      this.updateTime(value, 'milliseconds');
    } else {
      this.fillFieldsForTime(this.time);
    }
  }

  private updateTime(value: number, type: string): void {
    let date: Date;
    if (this.time === 0) {
      date = new Date(0);
    } else {
      date = new Date(this.time);
    }
    if (type === 'hours') {
      date.setUTCHours(value);
    }
    if (type === 'minutes') {
      date.setUTCMinutes(value);
    }
    if (type === 'seconds') {
      date.setUTCSeconds(value);
    }
    if (type === 'milliseconds') {
      date.setUTCMilliseconds(value);
    }
    this.time = date.getTime();
    this.fillFieldsForTime(this.time);
    this.fireUpdatedTime();
  }

  private fillFieldsForTime(time: number): void {
    if (time !== 0) {
      let date = new Date(this.time);
      this.hours = ('0' + date.getUTCHours()).slice(-2);
      this.minutes = ('0' + date.getUTCMinutes()).slice(-2);
      this.seconds = ('0' + date.getUTCSeconds()).slice(-2);
      this.milliSeconds = ('000' + date.getUTCMilliseconds()).slice(-3);
    } else {
      this.hours = null;
      this.minutes = null;
      this.seconds = null;
      this.milliSeconds = null;
    }
  }

  private fireUpdatedTime(): void {
    this.updatedTime.next(this.time);
  }
}
