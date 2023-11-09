import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeOfHabitat } from '../type-of-habitat.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-of-habitat.test-samples';

import { TypeOfHabitatService } from './type-of-habitat.service';

const requireRestSample: ITypeOfHabitat = {
  ...sampleWithRequiredData,
};

describe('TypeOfHabitat Service', () => {
  let service: TypeOfHabitatService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeOfHabitat | ITypeOfHabitat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeOfHabitatService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TypeOfHabitat', () => {
      const typeOfHabitat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeOfHabitat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeOfHabitat', () => {
      const typeOfHabitat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeOfHabitat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeOfHabitat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeOfHabitat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeOfHabitat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeOfHabitatToCollectionIfMissing', () => {
      it('should add a TypeOfHabitat to an empty array', () => {
        const typeOfHabitat: ITypeOfHabitat = sampleWithRequiredData;
        expectedResult = service.addTypeOfHabitatToCollectionIfMissing([], typeOfHabitat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeOfHabitat);
      });

      it('should not add a TypeOfHabitat to an array that contains it', () => {
        const typeOfHabitat: ITypeOfHabitat = sampleWithRequiredData;
        const typeOfHabitatCollection: ITypeOfHabitat[] = [
          {
            ...typeOfHabitat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeOfHabitatToCollectionIfMissing(typeOfHabitatCollection, typeOfHabitat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeOfHabitat to an array that doesn't contain it", () => {
        const typeOfHabitat: ITypeOfHabitat = sampleWithRequiredData;
        const typeOfHabitatCollection: ITypeOfHabitat[] = [sampleWithPartialData];
        expectedResult = service.addTypeOfHabitatToCollectionIfMissing(typeOfHabitatCollection, typeOfHabitat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfHabitat);
      });

      it('should add only unique TypeOfHabitat to an array', () => {
        const typeOfHabitatArray: ITypeOfHabitat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeOfHabitatCollection: ITypeOfHabitat[] = [sampleWithRequiredData];
        expectedResult = service.addTypeOfHabitatToCollectionIfMissing(typeOfHabitatCollection, ...typeOfHabitatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeOfHabitat: ITypeOfHabitat = sampleWithRequiredData;
        const typeOfHabitat2: ITypeOfHabitat = sampleWithPartialData;
        expectedResult = service.addTypeOfHabitatToCollectionIfMissing([], typeOfHabitat, typeOfHabitat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfHabitat);
        expect(expectedResult).toContain(typeOfHabitat2);
      });

      it('should accept null and undefined values', () => {
        const typeOfHabitat: ITypeOfHabitat = sampleWithRequiredData;
        expectedResult = service.addTypeOfHabitatToCollectionIfMissing([], null, typeOfHabitat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeOfHabitat);
      });

      it('should return initial array if no TypeOfHabitat is added', () => {
        const typeOfHabitatCollection: ITypeOfHabitat[] = [sampleWithRequiredData];
        expectedResult = service.addTypeOfHabitatToCollectionIfMissing(typeOfHabitatCollection, undefined, null);
        expect(expectedResult).toEqual(typeOfHabitatCollection);
      });
    });

    describe('compareTypeOfHabitat', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeOfHabitat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeOfHabitat(entity1, entity2);
        const compareResult2 = service.compareTypeOfHabitat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeOfHabitat(entity1, entity2);
        const compareResult2 = service.compareTypeOfHabitat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeOfHabitat(entity1, entity2);
        const compareResult2 = service.compareTypeOfHabitat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
