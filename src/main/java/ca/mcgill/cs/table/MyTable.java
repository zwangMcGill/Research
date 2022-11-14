package ca.mcgill.cs.table;

import ca.mcgill.cs.utils.ParsingUtil;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Function;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.ui.table.TableModelEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

public class MyTable {

    private TableModelEditor<MyRow> editor;

    private JComponent table;

    public JComponent getBrowsersTable() {
        return table;
    }

    private static final TableModelEditor.EditableColumnInfo<MyRow, Boolean> selectOptColumn = new TableModelEditor.EditableColumnInfo<MyRow, Boolean>("Select") {
        @Override
        public Class getColumnClass() {
            return Boolean.class;
        }

        @Override
        public Boolean valueOf(MyRow item) {
            return item.isActive();
        }

        @Override
        public void setValue(MyRow item, Boolean value) {
            item.setActive(value);
        }
    };

    private static final TableModelEditor.EditableColumnInfo<MyRow, String> nameColumn = new TableModelEditor.EditableColumnInfo<MyRow, String>("Name") {
        @Override
        public String valueOf(MyRow item) {
            return item.getName();
        }

        @Override
        public void setValue(MyRow item, String value) {
            item.setName(value);
        }

        /**
         * Comparator
         */
        @NotNull
        @Override
        public Comparator<MyRow> getComparator() {
            return new Comparator<MyRow>() {
                @Override
                public int compare(MyRow o1, MyRow o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
    };

    private static final TableModelEditor.EditableColumnInfo<MyRow, String> focalMethodColumn = new TableModelEditor.EditableColumnInfo<MyRow, String>("Focal Method") {
        @Override
        public String valueOf(MyRow item) {
            return item.getFocalMehtod();
        }

        @Override
        public void setValue(MyRow item, String value) {
            item.setFocalMethod(value);
        }

        /**
         * Comparator
         */
        @NotNull
        @Override
        public Comparator<MyRow> getComparator() {
            return new Comparator<MyRow>() {
                @Override
                public int compare(MyRow o1, MyRow o2) {
                    return o1.getFocalMehtod().compareTo(o2.getFocalMehtod());

                }
            };
        }
    };

    private static final TableModelEditor.EditableColumnInfo<MyRow, String> scenarioColumn = new TableModelEditor.EditableColumnInfo<MyRow, String>("Scenario") {
        @Override
        public String valueOf(MyRow item) {
            return item.getScenario();
        }

        @Override
        public void setValue(MyRow item, String value) {
            item.setScenario(value);
        }

        /**
         * Comparator
         */
        @NotNull
        @Override
        public Comparator<MyRow> getComparator() {
            return new Comparator<MyRow>() {
                @Override
                public int compare(MyRow o1, MyRow o2) {
                    return o1.getScenario().compareTo(o2.getScenario());
                }
            };
        }
    };

    private static final TableModelEditor.EditableColumnInfo<MyRow, String> expectedResultColumn = new TableModelEditor.EditableColumnInfo<MyRow, String>("Expected Result") {
        @Override
        public String valueOf(MyRow item) {
            return item.getExpectedResult();
        }

        @Override
        public void setValue(MyRow item, String value) {
            item.setExpectedResult(value);
        }

        /**
         * Comparator
         */
        @NotNull
        @Override
        public Comparator<MyRow> getComparator() {
            return new Comparator<MyRow>() {
                @Override
                public int compare(MyRow o1, MyRow o2) {
                    return o1.getExpectedResult().compareTo(o2.getExpectedResult());
                }
            };
        }
    };

    private static final ColumnInfo[] COLUMNS = {
            selectOptColumn,
            nameColumn,
            focalMethodColumn,
            scenarioColumn,
            expectedResultColumn,
            };


    public MyTable(VirtualFile vFile) {
        TableModelEditor.DialogItemEditor<MyRow> itemEditor = new TableModelEditor.DialogItemEditor<MyRow>() {
            @NotNull
            @Override
            public Class<MyRow> getItemClass() {
                return MyRow.class;
            }

            @Override
            public MyRow clone(@NotNull MyRow item, boolean forInPlaceEditing) {
                return new MyRow(forInPlaceEditing ? item.getId() : UUID.randomUUID(),
                        item.getName(), item.getFocalMehtod(), item.getScenario(), item.getExpectedResult(), item.isActive());
            }

            @Override
            public void edit(@NotNull MyRow browser, @NotNull Function<? super MyRow, ? extends MyRow> mutator, boolean isAdd) {

            }

            @Override
            public void applyEdited(@NotNull MyRow oldItem, @NotNull MyRow newItem) {

            }

            @Override
            public boolean isEditable(@NotNull MyRow browser) {
                return false;
            }

        };


        TableModelEditor.DataChangedListener<MyRow> dataChangedListener = new TableModelEditor.DataChangedListener<MyRow>() {
            @Override
            public void tableChanged(@NotNull TableModelEvent event) {
            }

            @Override
            public void dataChanged(@NotNull ColumnInfo<MyRow, ?> columnInfo, int rowIndex) {
            }
        };

        CompilationUnit cu = StaticJavaParser.parse(ParsingUtil.getSelectedFileObject(vFile));
        ParsingUtil parser = new ParsingUtil(cu);

        Map<MethodDeclaration, Map<String,String>> properties = parser.getAnnotationProperty();

        editor = new TableModelEditor<>(COLUMNS, itemEditor, "No tokens");
        editor.modelListener(dataChangedListener);

        ListTableModel<MyRow> model = this.editor.getModel();

        for(var entry : properties.entrySet()){
            MyRow myRow = new MyRow(
                UUID.randomUUID(),
                entry.getKey().getNameAsString(),
                entry.getValue().get("focalMethod"),
                entry.getValue().get("scenario"),
                entry.getValue().get("expectedResult"),
                true
            );
            model.addRow(myRow);
        }

        table = editor.createComponent();
    }

}
