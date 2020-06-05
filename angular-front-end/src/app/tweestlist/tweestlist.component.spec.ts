import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TweestlistComponent } from './tweestlist.component';

describe('TweestlistComponent', () => {
  let component: TweestlistComponent;
  let fixture: ComponentFixture<TweestlistComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TweestlistComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TweestlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
