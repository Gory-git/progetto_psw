import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormMiglioramentoComponent } from './form-miglioramento.component';

describe('FormMiglioramentoComponent', () => {
  let component: FormMiglioramentoComponent;
  let fixture: ComponentFixture<FormMiglioramentoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormMiglioramentoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormMiglioramentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
