import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiglioramentiComponent } from './miglioramenti.component';

describe('MiglioramentiComponent', () => {
  let component: MiglioramentiComponent;
  let fixture: ComponentFixture<MiglioramentiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiglioramentiComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MiglioramentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
