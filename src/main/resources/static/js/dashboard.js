document.addEventListener('DOMContentLoaded', function() {
    // Toggle sidebar
    const toggleSidebarBtn = document.querySelector('.toggle-sidebar-btn');
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    if (toggleSidebarBtn) {
        toggleSidebarBtn.addEventListener('click', function() {
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');

            // Update icon
            const icon = toggleSidebarBtn.querySelector('i');
            if (sidebar.classList.contains('collapsed')) {
                icon.classList.remove('fa-chevron-left');
                icon.classList.add('fa-chevron-right');
            } else {
                icon.classList.remove('fa-chevron-right');
                icon.classList.add('fa-chevron-left');
            }
        });
    }

    // Task checkbox functionality
    const taskCheckboxes = document.querySelectorAll('.task-checkbox');
    taskCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('click', function() {
            const taskItem = this.closest('.task-item');
            const icon = this.querySelector('i');

            if (!taskItem.classList.contains('completed')) {
                taskItem.classList.add('completed');
                icon.classList.remove('fa-regular', 'fa-circle');
                icon.classList.add('fa-solid', 'fa-check-circle');
            } else {
                taskItem.classList.remove('completed');
                icon.classList.remove('fa-solid', 'fa-check-circle');
                icon.classList.add('fa-regular', 'fa-circle');
            }
        });
    });

    // Tab switching
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            // Remove active class from all tabs
            tabs.forEach(t => t.classList.remove('active'));

            // Add active class to clicked tab
            this.classList.add('active');

            // Here you would typically filter tasks or show different content
            // based on the selected tab
        });
    });

    // Initialize circular progress charts
    initializeProgressCircles();

    // Initialize gauge chart
    initializeGauge();

    // Filter dropdowns
    const filterDropdowns = document.querySelectorAll('.filter-dropdown');
    filterDropdowns.forEach(dropdown => {
        dropdown.addEventListener('click', function() {
            // Here you would typically show a dropdown menu with filter options
            // For this demo, we just change the chevron icon
            const icon = this.querySelector('i');
            if (icon.classList.contains('fa-chevron-down')) {
                icon.classList.remove('fa-chevron-down');
                icon.classList.add('fa-chevron-up');
            } else {
                icon.classList.remove('fa-chevron-up');
                icon.classList.add('fa-chevron-down');
            }
        });
    });

    // Add hover effect to project rows
    const projectRows = document.querySelectorAll('.table-row');
    projectRows.forEach(row => {
        row.addEventListener('mouseenter', function() {
            this.style.backgroundColor = '#f9f9f9';
        });

        row.addEventListener('mouseleave', function() {
            this.style.backgroundColor = '';
        });
    });
});

// Function to initialize circular progress
function initializeProgressCircles() {
    document.querySelectorAll('.progress-circle').forEach(circle => {
        if (!circle.querySelector('svg')) { // Only initialize if not already done
            const progress = parseInt(circle.dataset.progress || 0);
            const circumference = 2 * Math.PI * 18; // Circle radius = 18px
            const offset = circumference - (progress / 100) * circumference;

            // Create the SVG elements
            circle.innerHTML = `
                <svg class="circle-progress" width="40" height="40" viewBox="0 0 40 40">
                    <circle cx="20" cy="20" r="18" fill="none" stroke="#e6e6e6" stroke-width="4"></circle>
                    <circle class="progress-value" cx="20" cy="20" r="18" fill="none" stroke="${getProgressColor(progress)}" 
                            stroke-width="4" stroke-dasharray="${circumference}" 
                            stroke-dashoffset="${offset}" transform="rotate(-90 20 20)"></circle>
                </svg>
                <span class="progress-text" style="color: ${getProgressColor(progress)}">${progress}%</span>
            `;
        }
    });
}

// Function to initialize gauge chart
function initializeGauge() {
    const gaugeElement = document.querySelector('.gauge-value');
    if (gaugeElement) {
        const value = parseInt(gaugeElement.dataset.value || 0);
        const angle = (value / 100) * 180;
        gaugeElement.style.transform = `rotate(${angle}deg)`;
    }
}

// Helper function to get color based on progress
function getProgressColor(progress) {
    if (progress >= 90) return '#4CAF50'; // Green
    if (progress >= 50) return '#FF9800'; // Orange
    return '#F44336'; // Red
}

// Function to format currency values
function formatCurrency(value) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(value);
}

// Sample function to handle creating a new project
function createNewProject() {
    // This would typically open a modal or navigate to a new page
    console.log('Creating new project...');
    alert('Create new project functionality would open a form here.');
}

// Add event listener to create project button
document.addEventListener('DOMContentLoaded', function() {
    const createProjectBtn = document.querySelector('.create-project-btn');
    if (createProjectBtn) {
        createProjectBtn.addEventListener('click', createNewProject);
    }
});