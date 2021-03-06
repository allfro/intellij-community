package com.jetbrains.python.refactoring.classes.membersManager;

import com.jetbrains.python.psi.*;
import com.jetbrains.python.refactoring.classes.PyClassRefactoringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Moves class attributes up
 *
 * @author Ilya.Kazakevich
 */
class ClassFieldsManager extends FieldsManager {

  ClassFieldsManager() {
    super(true);
  }

  @Override
  public boolean hasConflict(@NotNull final PyTargetExpression member, @NotNull final PyClass aClass) {
    return NamePredicate.hasElementWithSameName(member, aClass.getClassAttributes());
  }

  @Override
  protected Collection<PyElement> moveAssignments(@NotNull final PyClass from,
                                                  @NotNull final Collection<PyAssignmentStatement> statements,
                                                  @NotNull final PyClass... to) {
    //TODO: Copy/paste with InstanceFieldsManager. Move to parent?
    final List<PyElement> result = new ArrayList<PyElement>();
    for (final PyClass destClass : to) {
      result.addAll(PyClassRefactoringUtil.copyFieldDeclarationToStatement(statements, destClass.getStatementList()));
    }
    deleteElements(statements);
    PyClassRefactoringUtil.insertPassIfNeeded(from);
    return result;
  }

  @Override
  protected boolean classHasField(@NotNull final PyClass pyClass, @NotNull final String fieldName) {
    return pyClass.findClassAttribute(fieldName, true) != null;
  }

  @NotNull
  @Override
  protected List<PyTargetExpression> getFieldsByClass(@NotNull final PyClass pyClass) {
    return pyClass.getClassAttributes();
  }
}
