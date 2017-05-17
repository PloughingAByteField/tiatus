import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Subscription } from 'rxjs/Subscription';

import { Club } from '../../clubs/club.model';
import { AdminClubsService } from './clubs.service';

@Component({
  selector: 'clubs',
  styleUrls: [ './clubs.component.css' ],
  templateUrl: './clubs.component.html'
})
export class ClubsComponent implements OnInit, OnDestroy {
  public clubs: Club[];
  public addClubForm: FormGroup;
  public clubsForm: FormGroup;

  private clubsSubscription: Subscription;

  constructor(
    private clubsService: AdminClubsService,
    private fb: FormBuilder
  ) {}

  public ngOnInit() {
    this.clubsForm = this.fb.group({
      clubs: this.fb.array([])
    });

    this.addClubForm = this.fb.group({
      newName: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250),
        this.existingClubName()
      ])
    });

    this.clubsSubscription = this.clubsService.getClubs()
      .subscribe((clubs: Club[]) => {
        this.clubs = clubs;
        this.populateClubsForm(this.clubs);
      });
  }

  public ngOnDestroy() {
    this.clubsSubscription.unsubscribe();
  }

  public removeClub(clubControlGroup: FormGroup): void {
    const clubToRemove: Club = clubControlGroup.get('club').value;
    this.clubsService.removeClub(clubToRemove);
  }

  public updateClub(clubControlGroup: FormGroup): void {
    const newName: string = clubControlGroup.get('name').value;
    const clubToUpdate: Club = clubControlGroup.get('club').value;
    const updatedClub: Club = new Club();
    updatedClub.clubName = newName;
    updatedClub.id = clubToUpdate.id;
    this.clubsService.updateClub(updatedClub);
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    const newClub: Club = new Club();
    newClub.clubName = value.newName;
    this.clubsService.createClub(newClub).then((club: Club) => this.addClubForm.reset());
  }

  public getClubsFormArray(): FormArray {
    if (this.clubsForm) {
      return this.clubsForm.get('clubs') as FormArray;
    }
  }

  private populateClubsForm(clubs): void {
    const newClubsArray: FormArray = this.fb.array([]);
    this.clubs.map((club: Club) => {
      const clubControl: FormControl = this.fb.control(club);
      const nameControl: FormControl = this.fb.control(club.clubName, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250),
        this.existingClubName()
      ]);
      const group: FormGroup = this.fb.group({
        name: nameControl,
        club: clubControl
      });
      newClubsArray.push(group);
    });
    this.clubsForm.setControl('clubs', newClubsArray);
  }

  private existingClubName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.clubs) {
        for (const club of this.clubs) {
          if (club.clubName === control.value) {
            return { existingClubName: true };
          }
        }
      }

      return null;
    };
  }
}
