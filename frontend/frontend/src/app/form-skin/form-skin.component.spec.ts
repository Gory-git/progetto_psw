import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormSkinComponent } from './form-skin.component';

describe('FormSkinComponent', () => {
  let component: FormSkinComponent;
  let fixture: ComponentFixture<FormSkinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormSkinComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormSkinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
